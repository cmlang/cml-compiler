package cml.language.expressions;

import cml.language.types.TempNamedType;

public class Literal extends ExpressionBase
{
    private final String text;
    private final TempNamedType type;

    public Literal(String text, TempNamedType type)
    {
        this.text = text;
        this.type = type;
    }

    public String getText()
    {
        return this.text;
    }

    public TempNamedType getType()
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
