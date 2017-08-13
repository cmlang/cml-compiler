package cml.language.expressions;

import cml.language.foundation.Type;

import java.util.List;

public class Comprehension extends ExpressionBase
{
    private final List<Enumerator> enumerators;
    private final Expression expression;

    public Comprehension(List<Enumerator> enumerators, Expression expression)
    {
        this.enumerators = enumerators;
        this.expression = expression;
    }

    public List<Enumerator> getEnumerators()
    {
        return enumerators;
    }

    public Expression getExpression()
    {
        return expression;
    }

    @Override
    public String getKind()
    {
        return "comprehension";
    }

    @Override
    public Type getType()
    {
        return expression.getType();
    }
}
