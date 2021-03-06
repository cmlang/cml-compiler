package cml.language.expressions;

import cml.language.features.Function;
import cml.language.features.FunctionParameter;
import cml.language.features.TempModule;
import cml.language.generated.Concept;
import cml.language.generated.Expression;
import cml.language.generated.NamedElement;
import cml.language.generated.Type;
import cml.language.types.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;

import static cml.language.functions.ModelElementFunctions.moduleOf;
import static cml.language.functions.TypeFunctions.*;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.UndefinedType.createUndefinedType;
import static java.lang.String.format;
import static java.util.Collections.*;
import static java.util.stream.Collectors.toMap;
import static org.jooq.lambda.Seq.seq;

public interface Invocation extends Expression, NamedElement
{
    String MESSAGE__UNABLE_TO_FIND_FUNCTION_OF_INVOCATION = "Unable to find function of invocation: ";
    String MESSAGE__SHOULD_MATCH_NUMBER_OF_PARAMS_IN_FUNCTION = "Number of arguments in invocation should match the number of parameters in function: ";
    String MESSAGE__SHOULD_MATCH_PARAMETER_TYPE_IN_FUNCTION = "Argument type should match parameter type in function: ";

    List<Expression> getArguments();
    Map<String, Expression> getNamedArguments();

    default List<FunctionParameter> getParameters()
    {
        if (getFunction().isPresent())
        {
            return getFunction().get().getParameters();
        }
        else if (getConcept().isPresent())
        {
            final Concept concept = getConcept().get();

            return seq(concept.getInvocationProperties())
                .map(p -> new FunctionParameter(p.getName(), p.getType()))
                .toList();
        }
        else
        {
            return emptyList();
        }
    }

    default Map<FunctionParameter, Expression> getParameterizedArguments()
    {
        return seq(getParameters())
            .zip(getArguments())
            .collect(toMap(Tuple2::v1, Tuple2::v2));
    }

    default Map<FunctionType, Lambda> getTypedLambdaArguments()
    {
        return seq(getParameterizedArguments()).filter(t -> t.v1.getType() instanceof FunctionType)
                                               .filter(t -> t.v2 instanceof Lambda)
                                               .map(t -> new Tuple2<>((FunctionType) t.v1.getType(), (Lambda) t.v2))
                                               .collect(toMap(Tuple2::v1, Tuple2::v2));
    }

    default Map<Type, Lambda> getUntypedParameterlessLambdaArguments()
    {
        return seq(getParameterizedArguments()).filter(t -> !(t.v1.getType() instanceof FunctionType))
                                               .filter(t -> t.v2 instanceof Lambda)
                                               .map(t -> new Tuple2<>(t.v1.getType(), (Lambda) t.v2))
                                               .filter(t -> t.v2.getParameters().count() == 0)
                                               .collect(toMap(Tuple2::v1, Tuple2::v2));
    }

    Optional<Function> getFunction();
    void setFunction(@NotNull Function function);

    Optional<Concept> getConcept();
    void setConcept(@NotNull Concept concept);

    default Type getInferredType()
    {
        if (getFunction().isPresent())
        {
            final Type resultType = getFunction().get().getType();

            return getMatchingTypeOf(resultType);
        }
        else if (getConcept().isPresent())
        {
            final Concept concept = getConcept().get();
            final TempNamedType namedType = TempNamedType.create(concept.getName());

            namedType.setConcept(concept);

            return namedType;
        }
        else
        {
            return createUndefinedType(MESSAGE__UNABLE_TO_FIND_FUNCTION_OF_INVOCATION + getName());
        }
    }

    default Type getMatchingTypeOf(final Type type)
    {
        if (getParameters().size() == getArguments().size())
        {
            if (type instanceof TupleType)
            {
                final TupleType tupleType = (TupleType) type;
                final Seq<TupleTypeElement> matchingElements = tupleType.getElements()
                                                                        .map(e -> new TupleTypeElement(getMatchingTypeOf(e.getType()), e.getName().orElse(null)));

                return new TupleType(matchingElements);
            }
            else if (type.isParameter())
            {
                int paramIndex = getParamIndexOfMatchingType(getParameters(), type);

                if (paramIndex < getArguments().size())
                {
                    Type paramType = getArguments().get(paramIndex).getMatchingResultType();

                    if (paramType.isUndefined())
                    {
                        paramIndex = getParamIndexOfMatchingType(getParameters(), type, paramIndex);

                        if (paramIndex < getArguments().size())
                        {
                            paramType = getArguments().get(paramIndex).getMatchingResultType();
                        }
                    }

                    if (paramType instanceof TupleType && type instanceof MemberType)
                    {
                        final TupleType tupleType = (TupleType) paramType;
                        final MemberType memberType = (MemberType) type;

                        paramType = tupleType.getElementTypes()
                                             .get(memberType.getParamIndex())
                                             .orElse(createUndefinedType(MESSAGE__SHOULD_MATCH_PARAMETER_TYPE_IN_FUNCTION + getName()));
                    }

                    return withCardinality(paramType, type.getCardinality());
                }
                else
                {
                    return createUndefinedType(MESSAGE__SHOULD_MATCH_PARAMETER_TYPE_IN_FUNCTION + getName());
                }
            }
            else
            {
                return type;
            }
        }
        else
        {
            return createUndefinedType(MESSAGE__SHOULD_MATCH_NUMBER_OF_PARAMS_IN_FUNCTION + getName());
        }
    }

    default void createScopeFor(Lambda lambda)
    {
        assert lambda.getFunctionType().isPresent() || lambda.getParameters().count() == 0;
        assert !lambda.isInnerExpressionInSomeScope();

        final Optional<Type> scopeType = lambda.getExpectedScopeType();

        if (scopeType.isPresent())
        {
            final Type matchingType = getMatchingTypeOf(scopeType.get());

            assert matchingType.getConcept().isPresent(): "Expected concept but found '" + matchingType.getDiagnosticId() + "' for lambda: " + lambda.getDiagnosticId() + " - " + scopeType.get().getDiagnosticId();

            final Optional<Concept> concept = matchingType.getConcept();

            assert concept.isPresent();

            new LambdaScope(concept.get(), lambda);
        }
        else
        {
            final Optional<TempModule> module = moduleOf(this);

            assert module.isPresent();

            final LambdaScope lambdaScope = new LambdaScope(this, lambda);

            if (lambda.getFunctionType().isPresent())
            {
                lambda.getTypedParameters()
                      .forEach((name, type) -> lambdaScope.addParameter(name, getMatchingTypeOf(type)));
            }
        }
    }

    default boolean typeMatches(final FunctionParameter param, final Expression argument)
    {
        final Type paramType = param.getMatchingResultType();
        final Type argumentType = argument.getMatchingResultType();

        return !argumentType.isUndefined() && isAssignableFrom(getMatchingTypeOf(paramType), argumentType);
    }

    static Invocation create(String name, List<Expression> arguments)
    {
        return new InvocationImpl(name, arguments);
    }

    static Invocation create(String name, LinkedHashMap<String, Expression> namedArguments)
    {
        return new ParameterizedInvocation(name, namedArguments);
    }
}

class InvocationImpl extends ExpressionBase implements Invocation
{
    private final NamedElement namedElement;

    private final List<Expression> arguments;

    private @Nullable Function function;
    private @Nullable Concept concept;

    InvocationImpl(String name, List<Expression> arguments)
    {
        super(seq(arguments).toList());

        namedElement = extendNamedElement(this, expression, expression, name);

        this.arguments = new ArrayList<>(arguments);
    }

    @Override
    public List<Expression> getArguments()
    {
        return unmodifiableList(arguments);
    }

    @Override
    public Map<String, Expression> getNamedArguments()
    {
        return seq(getParameters())
            .zip(getArguments())
            .collect(toMap(t -> t.v1().getName(), Tuple2::v2));
    }

    @Override
    public Optional<Function> getFunction()
    {
        return Optional.ofNullable(function);
    }

    @Override
    public void setFunction(@NotNull Function function)
    {
        assert this.function == null;

        this.function = function;
    }

    @Override
    public Optional<Concept> getConcept()
    {
        return Optional.ofNullable(concept);
    }

    @Override
    public void setConcept(@NotNull final Concept concept)
    {
        assert this.concept == null;

        this.concept = concept;
    }

    @Override
    public String getKind()
    {
        return "invocation";
    }

    @Override
    public Type getType()
    {
        return getInferredType();
    }

    @Override
    public Type getMatchingResultType()
    {
        return expression.getMatchingResultType();
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public String getDiagnosticId()
    {
        return format("%s(%s) -> %s", getName(), seq(arguments).map(a -> a.getDiagnosticId()).toString(", "), getType().getDiagnosticId());
    }
}

class ParameterizedInvocation extends ExpressionBase implements Invocation
{
    private final NamedElement namedElement;

    private final LinkedHashMap<String, Expression> namedArguments;

    private @Nullable Function function;
    private @Nullable Concept concept;

    ParameterizedInvocation(String name, LinkedHashMap<String, Expression> namedArguments)
    {
        super(seq(namedArguments.values()).toList());

        namedElement = extendNamedElement(this, expression, expression, name);

        this.namedArguments = new LinkedHashMap<>(namedArguments);
    }

    @Override
    public List<Expression> getArguments()
    {
        return new ArrayList<>(namedArguments.values());
    }

    @Override
    public Map<String, Expression> getNamedArguments()
    {
        return unmodifiableMap(namedArguments);
    }

    @Override
    public Optional<Function> getFunction()
    {
        return Optional.ofNullable(function);
    }

    @Override
    public void setFunction(@NotNull Function function)
    {
        assert this.function == null;

        this.function = function;
    }

    @Override
    public Optional<Concept> getConcept()
    {
        return Optional.ofNullable(concept);
    }

    @Override
    public void setConcept(@NotNull final Concept concept)
    {
        this.concept = concept;
    }

    @Override
    public String getKind()
    {
        return "invocation";
    }

    @Override
    public Type getType()
    {
        return getInferredType();
    }

    @Override
    public Type getMatchingResultType()
    {
        return expression.getMatchingResultType();
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public String getDiagnosticId()
    {
        final Seq<String> namedArguments = seq(getNamedArguments()).map(t -> format("%s: %s", t.v1, t.v2.getDiagnosticId()));

        return format("%s(%s) -> %s", getName(), namedArguments.toString(", "), getType().getDiagnosticId());
    }
}
