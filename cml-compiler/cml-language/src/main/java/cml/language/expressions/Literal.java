package cml.language.expressions;

import cml.language.types.NamedType;

public class Literal extends ExpressionBase
{
    private final String text;
    private final NamedType type;

    public Literal(String text, NamedType type)
    {
        this.text = text;
        this.type = type;
    }

    public String getText()
    {
        return this.text;
    }

    public NamedType getType()
    {
        return type;
    }

    @Override
    public String getKind()
    {
        return "literal";
    }

    @Override
    public String toString()
    {
        return text;
    }
}
