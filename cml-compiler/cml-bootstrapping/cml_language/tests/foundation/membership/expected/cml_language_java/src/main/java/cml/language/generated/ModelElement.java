package cml.language.generated;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface ModelElement
{
    Optional<Location> getLocation();

    Optional<Scope> getParent();

    static ModelElement extendModelElement(@Nullable Location location, @Nullable Scope parent)
    {
        return new ModelElementImpl(location, parent);
    }
}

class ModelElementImpl implements ModelElement
{
    private final @Nullable Location location;
    private final @Nullable Scope parent;

    ModelElementImpl(@Nullable Location location, @Nullable Scope parent)
    {
        this.location = location;
        this.parent = parent;
    }

    public Optional<Location> getLocation()
    {
        return Optional.ofNullable(this.location);
    }

    public Optional<Scope> getParent()
    {
        return Optional.ofNullable(this.parent);
    }

    public String toString()
    {
        return new StringBuilder(ModelElement.class.getSimpleName())
                   .append('(')
                   .append("location=").append(getLocation().isPresent() ? String.format("\"%s\"", getLocation()) : "not present").append(", ")
                   .append("parent=").append(getParent().isPresent() ? String.format("\"%s\"", getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}