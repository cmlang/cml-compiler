package cml.language.types;

import cml.language.generated.NamedElement;

public interface TypedElement extends NamedElement
{
    Type getType();

    default boolean matchesTypeOf(TypedElement other)
    {
        return this.getType().isAssignableFrom(other.getType());
    }
}

