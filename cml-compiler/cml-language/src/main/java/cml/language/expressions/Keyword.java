package cml.language.expressions;

import cml.language.foundation.NamedElementBase;

public class Keyword extends NamedElementBase
{
    private final Expression expression;

    public Keyword(final String name, final Expression expression)
    {
        super(name);
        this.expression = expression;
    }

    public Expression getExpression()
    {
        return expression;
    }
}
