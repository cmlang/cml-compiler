package cml.language.generated;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface ModelElement
{
    Optional<Scope> getParent();

    Optional<Location> getLocation();

    static ModelElement createModelElement(@Nullable Scope parent, @Nullable Location location)
    {
        return new ModelElementImpl(null, parent, location);
    }

    static ModelElement extendModelElement(@Nullable ModelElement actual_self, @Nullable Scope parent, @Nullable Location location)
    {
        return new ModelElementImpl(actual_self, parent, location);
    }
}

class ModelElementImpl implements ModelElement
{
    private static Membership membership;
    private static Localization localization;

    private final @Nullable ModelElement actual_self;

    ModelElementImpl(@Nullable ModelElement actual_self, @Nullable Scope parent, @Nullable Location location)
    {
        this.actual_self = actual_self == null ? this : actual_self;



        membership.link(parent, this.actual_self);
        localization.link(location, this.actual_self);
    }

    public Optional<Scope> getParent()
    {
        return membership.parentOf(actual_self);
    }

    public Optional<Location> getLocation()
    {
        return localization.locationOf(actual_self);
    }

    public String toString()
    {
        return new StringBuilder(ModelElement.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }

    static void setMembership(Membership association)
    {
        membership = association;
    }

    static void setLocalization(Localization association)
    {
        localization = association;
    }

    static
    {
        Membership.init(ModelElement.class);
        Localization.init(ModelElement.class);
    }
}