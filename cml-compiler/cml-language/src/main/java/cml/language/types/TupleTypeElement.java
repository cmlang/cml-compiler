package cml.language.types;

import cml.language.foundation.ModelElementBase;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class TupleTypeElement extends ModelElementBase
{
    private final Type type;
    private final @Nullable String name;

    public TupleTypeElement(final Type type, final @Nullable String name)
    {
        this.type = type;
        this.name = name;
    }

    public Type getType()
    {
        return type;
    }

    public Optional<String> getName()
    {
        return ofNullable(name);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TupleTypeElement that = (TupleTypeElement) o;

        if (type != null ? type.equals(that.type) : that.type == null)
            if (name != null ? name.equals(that.name) : that.name == null) return true;

        return false;
    }

    @Override
    public int hashCode()
    {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return name == null ? type.toString() : format("%s: %s", name, type);
    }
}
