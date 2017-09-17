package cml.language.types;

import cml.language.generated.NamedElement;

import static cml.language.functions.TypeFunctions.isAssignableFrom;

public interface TypedElement extends NamedElement
{
    Type getType();

    default boolean matchesTypeOf(TypedElement other)
    {
        return isAssignableFrom(this.getType(), other.getType());
    }
}

