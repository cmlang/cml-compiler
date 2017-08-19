package cml.language.types;

import cml.language.foundation.NamedElementBase;

public abstract class TypedElementBase extends NamedElementBase implements TypedElement
{
    private final Type type;

    public TypedElementBase(final String name, final Type type)
    {
        super(name);
        this.type = type;
    }

    @Override
    public Type getType()
    {
        return type;
    }
}
