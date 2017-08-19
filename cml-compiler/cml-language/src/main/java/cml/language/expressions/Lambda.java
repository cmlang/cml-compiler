package cml.language.expressions;

import cml.language.types.NamedType;

import java.util.List;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;

public class Lambda extends ExpressionBase
{
    private final List<String> parameters;
    private final Expression expression;

    public Lambda(final List<String> parameters, final Expression expression)
    {
        this.parameters = parameters;
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

    @Override
    public String getKind()
    {
        return "lambda";
    }

    @Override
    public NamedType getType()
    {
        return expression.getType();
    }

    @Override
    public String toString()
    {
        return format("Lambda { %s -> %s }", parameters, expression);
    }
}

