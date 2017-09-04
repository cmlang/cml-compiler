package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.types.FunctionType;
import cml.language.types.MemberType;
import cml.language.types.NamedType;
import cml.language.types.Type;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static cml.language.functions.ScopeFunctions.typeOfVariableNamed;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Optional.*;
import static java.util.stream.Collectors.toMap;
import static org.jooq.lambda.Seq.seq;

public class Lambda extends ExpressionBase
{
    private final List<String> parameters;
    private final Expression expression;

    private @Nullable FunctionType functionType;

    public Lambda(final Seq<String> parameters, final Expression expression)
    {
        this.parameters = parameters.toList();
        this.expression = expression;
    }

    public Seq<String> getParameters()
    {
        return seq(parameters);
    }

    public Expression getExpression()
    {
        return expression;
    }

    @Override
    public Seq<Expression> getSubExpressions()
    {
        return Seq.of(expression);
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
            final Function<String, MemberType> mapping = p -> {
                assert getParameters().indexOf(p).isPresent();

                return new MemberType(functionType.getSingleParamType(), p, getParameters().indexOf(p).getAsLong());
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
        return getTypeUndefinedParams().zip(getTypeUndefinedParams().map(p -> NamedType.UNDEFINED));
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
        return functionType == null ? NamedType.createUndefined("Function type not specified for: " + this) : functionType;
    }

    @Override
    public Type getMatchingResultType()
    {
        return expression.getType();
    }

    public boolean isExpressionInSomeScope()
    {
        return expression.getParentScope().isPresent();
    }

    public void addExpressionToScope(final Scope scope)
    {
        assert !isExpressionInSomeScope();

        scope.addMember(expression);
    }

    @Override
    public void addMember(final ModelElement member)
    {
        throw new UnsupportedOperationException("Lambda should not be used as scope. The scope of its expression is provided by addExpressionToScope().");
    }

    @Override
    public String toString()
    {
        return parameters.isEmpty() ?
            format("{ %s }", expression) :
            format("{ %s -> %s }", seq(parameters).map(this::stringOf).toString(", "), expression);
    }

    private String stringOf(final String parameter)
    {
        final Optional<Type> actualType = typeOfVariableNamed(parameter, expression);
        final Type formalType = getTypedParameters().get(parameter);

        return actualType.map(t -> parameter + ": " + t)
                   .orElseGet(() -> formalType == null ? parameter : formalType.toString());
    }
}

