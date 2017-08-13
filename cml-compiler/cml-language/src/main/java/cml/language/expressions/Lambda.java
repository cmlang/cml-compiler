package cml.language.expressions;

import cml.language.foundation.Type;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Lambda extends ExpressionBase
{
    private final List<String> variables;
    private final Expression expression;

    public Lambda(final List<String> variables, final Expression expression)
    {
        this.variables = variables;
        this.expression = expression;
    }

    private List<String> getVariables()
    {
        return unmodifiableList(variables);
    }

    private Expression getExpression()
    {
        return expression;
    }

    @Override
    public String getKind()
    {
        return "lambda";
    }

    @Override
    public Type getType()
    {
        return expression.getType();
    }
}

