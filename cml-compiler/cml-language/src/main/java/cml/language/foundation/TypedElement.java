package cml.language.foundation;

public interface TypedElement extends NamedElement
{
    Type getType();

    default boolean matchesTypeOf(TypedElement other)
    {
        return this.getType().equals(other.getType());
    }
}

