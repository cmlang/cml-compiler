package cml.language.expressions;

import cml.language.foundation.Scope;
import cml.language.types.FunctionType;
import cml.language.types.NamedType;
import cml.language.types.Type;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableList;
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

        addMember(expression);
    }

    public List<String> getParameters()
    {
        return unmodifiableList(parameters);
    }

    public Expression getExpression()
    {
        return expression;
    }

    public Optional<FunctionType> getFunctionType()
    {
        return ofNullable(functionType);
    }

    public void setFunctionType(@Nullable FunctionType functionType)
    {
        this.functionType = functionType;
    }

    public Map<String, Type> getTypedParameters()
    {
        if (functionType == null)
        {
            return emptyMap();
        }
        else
        {
            assert parameters.size() == functionType.getParamTypes().count();

            return seq(parameters).zip(functionType.getParamTypes())
                                  .collect(toMap(Tuple2::v1, Tuple2::v2));
        }
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

    public void addExpressionScope(final Scope scope)
    {
        scope.addMember(expression);
    }

    @Override
    public String toString()
    {
        return parameters.isEmpty() ?
            format("{ %s }", expression) :
            format("{ %s -> %s }", seq(parameters).toString(", "), expression);
    }
}

