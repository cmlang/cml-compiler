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

    Optional<Scope> getScope();

    static ModelElement createModelElement(@Nullable Location location, @Nullable Scope scope)
    {
        return new ModelElementImpl(location, scope);
    }

    static ModelElement extendModelElement(@Nullable Location location, @Nullable Scope scope)
    {
        return new ModelElementImpl(location, scope);
    }
}

class ModelElementImpl implements ModelElement
{
    private static Membership membership;

    private final @Nullable Location location;

    public ModelElementImpl(@Nullable Location location, @Nullable Scope scope)
    {
        this.location = location;

        membership.link(scope, this);
    }

    public Optional<Location> getLocation()
    {
        return Optional.ofNullable(this.location);
    }

    public Optional<Scope> getScope()
    {
        return membership.scopeOf(this);
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