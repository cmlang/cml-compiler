package cml.language.types;

import cml.language.foundation.NamedElement;

public interface TypedElement extends NamedElement
{
    NamedType getType();

    default boolean matchesTypeOf(TypedElement other)
    {
        return this.getType().equals(other.getType());
    }
}

