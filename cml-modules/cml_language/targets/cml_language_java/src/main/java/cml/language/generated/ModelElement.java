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

    static ModelElement createModelElement(@Nullable Location location, @Nullable Scope parent)
    {
        return new ModelElementImpl(location, parent);
    }

    static ModelElement extendModelElement(@Nullable Location location, @Nullable Scope parent)
    {
        return new ModelElementImpl(location, parent);
    }
}

class ModelElementImpl implements ModelElement
{
    private static Membership membership;

    private final @Nullable Location location;

    public ModelElementImpl(@Nullable Location location, @Nullable Scope parent)
    {
        this.location = location;

        membership.link(parent, this);
    }

    public Optional<Location> getLocation()
    {
        return Optional.ofNullable(this.location);
    }

    public Optional<Scope> getParent()
    {
        return membership.parentOf(this);
    }

    public String toString()
    {
        return new StringBuilder(ModelElement.class.getSimpleName())
                   .append('(')
                   .append("location=").append(getLocation().isPresent() ? String.format("\"%s\"", getLocation()) : "not present")
                   .append(')')
                   .toString();
    }

    static void setMembership(Membership association)
    {
        membership = association;
    }

    static
    {
        Membership.init(ModelElement.class);
    }
}