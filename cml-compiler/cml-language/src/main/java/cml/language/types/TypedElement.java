package cml.language.types;

import cml.language.foundation.NamedElement;

public interface TypedElement extends NamedElement
{
    Type getType();

    default boolean matchesTypeOf(TypedElement other)
    {
        return this.getType().equals(other.getType());
    }
}

