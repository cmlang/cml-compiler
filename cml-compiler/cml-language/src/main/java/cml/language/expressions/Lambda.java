package cml.language.expressions;

import cml.language.generated.Expression;
import cml.language.generated.Type;
import cml.language.types.FunctionType;
import cml.language.types.MemberType;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Function;

import static cml.language.functions.ScopeFunctions.typeOfVariableNamed;
import static cml.language.generated.UndefinedType.createUndefinedType;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Optional.*;
import static java.util.stream.Collectors.toMap;
import static org.jooq.lambda.Seq.seq;

public class Lambda extends ExpressionBase
{
    private final List<String> parameters;
    private final Expression innerExpression;

    private @Nullable FunctionType functionType;

    public Lambda(final Seq<String> parameters, final Expression innerExpression)
    {
        this.parameters = parameters.toList();
        this.innerExpression = innerExpression;
    }

    public Seq<String> getParameters()
    {
        return seq(parameters);
    }

    public Expression getInnerExpression()
    {
        return innerExpression;
    }

    @Override
    public List<Expression> getOperands()
    {
        return Seq.of(innerExpression).toList();
    }

    public Optional<FunctionType> getFunctionType()
    {
        return ofNullable(functionType);
    }

    public void setFunctionType(@Nullable FunctionType functionType)
    {
        assert this.functionType == null;

        this.functionType = functionType;
    }

    public Map<String, Type> getTypedParameters()
    {
        if (functionType == null)
        {
            return emptyMap();
        }
        if (parameters.size() == getParamTypeCount())
        {
            return getParameters().zip(getParamTypes())
                                  .collect(toMap(Tuple2::v1, Tuple2::v2));
        }
        else if (getParamTypeCount() == 1)
        {
            final Function<String, MemberType> mapping = paramName ->
            {
                final OptionalLong paramIndex = getParameters().indexOf(paramName);

                assert paramIndex.isPresent();

                return new MemberType(functionType.getSingleParamType(), paramName, paramIndex.getAsLong());
            };

            return getParameters().zip(getParameters().map(mapping))
                                  .collect(toMap(Tuple2::v1, Tuple2::v2));
        }
        else
        {
            return getTypeDefinedParams().zip(getParamTypes())
                                         .concat(getUntypedParams())
                                         .collect(toMap(Tuple2::v1, Tuple2::v2));
        }
    }

    public Seq<Tuple2<String, Type>> getUntypedParams()
    {
        return getTypeUndefinedParams().zip(getTypeUndefinedParams()
                                       .map(p -> createUndefinedType("Unable to infer type of parameter: " + p)));
    }

    public Seq<String> getTypeDefinedParams()
    {
        return getParameters().limit(getParamTypeCount());
    }

    public Seq<String> getTypeUndefinedParams()
    {
        return getParameters().skip(getParamTypeCount());
    }

    public Seq<Type> getParamTypes()
    {
        assert functionType != null;

        return functionType.getParamTypes();
    }

    public long getParamTypeCount()
    {
        return getParamTypes().count();
    }

    public Optional<Type> getExpectedScopeType()
    {
        if (parameters.isEmpty() && functionType != null && functionType.isSingleParam())
        {
            return of(functionType.getSingleParamType());
        }
        else
        {
            return empty();
        }
    }

    @Override
    public String getKind()
    {
        return "lambda";
    }

    @Override
    public Type getType()
    {
        return functionType == null ? createUndefinedType("Function type not specified for: " + getDiagnosticId()) : functionType;
    }

    @Override
    public Type getMatchingResultType()
    {
        return (Type) innerExpression.getType();
    }

    public boolean isInnerExpressionInSomeScope()
    {
        return innerExpression.getParent().isPresent();
    }

    @Override
    public String getDiagnosticId()
    {
        return parameters.isEmpty() ?
            format("{ %s }", innerExpression.getDiagnosticId()) :
            format("{ %s -> %s }", seq(parameters).map(this::stringOf).toString(", "), innerExpression.getDiagnosticId())
            + " - inferred result type: " + getMatchingResultType().getDiagnosticId();
    }

    private String stringOf(final String parameter)
    {
        final Optional<Type> actualType = typeOfVariableNamed(parameter, innerExpression);
        final Type formalType = getTypedParameters().get(parameter);

        return actualType.map(t -> parameter + ": " + t.getDiagnosticId())
                         .orElseGet(() -> formalType == null ? parameter : formalType.getDiagnosticId());
    }
}

