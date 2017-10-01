package cml.language.types;

import cml.language.foundation.NamedElementBase;
import cml.language.generated.TypedElement;

public abstract class TypedElementBase extends NamedElementBase implements TypedElement
{
    private final TempType type;

    public TypedElementBase(final String name, final TempType type)
    {
        super(name);
        this.type = type;
    }

    @Override
    public TempType getType()
    {
        return (TempType) type;
    }
}
