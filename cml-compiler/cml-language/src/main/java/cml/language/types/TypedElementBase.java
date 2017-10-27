package cml.language.types;

import cml.language.foundation.NamedElementBase;
import cml.language.generated.Scope;
import cml.language.generated.Type;
import cml.language.generated.TypedElement;
import org.jetbrains.annotations.Nullable;

public abstract class TypedElementBase extends NamedElementBase implements TypedElement
{
    private final Type type;

    public TypedElementBase(final String name, final Type type)
    {
        this(null, name, type);
    }

    public TypedElementBase(@Nullable final Scope parent, final String name, final Type type)
    {
        super(parent, name);

        this.type = type;
    }

    @Override
    public Type getType()
    {
        return type;
    }
}
