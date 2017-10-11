package cml.language.types;

import cml.language.foundation.ModelElementBase;
import cml.language.functions.TypeFunctions;
import cml.language.generated.Type;
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

    public boolean isAssignableFrom(final TupleTypeElement that)
    {
        return TypeFunctions.isAssignableFrom(this.type, that.type) &&
               (name != null ? name.equals(that.name) : that.name == null);
    }

    @Override
    public String toString()
    {
        return name == null ? type.toString() : format("%s: %s", name, type);
    }
}
