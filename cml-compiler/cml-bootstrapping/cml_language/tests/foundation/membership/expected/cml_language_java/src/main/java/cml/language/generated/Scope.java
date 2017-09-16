package cml.language.generated;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Scope extends ModelElement
{
    List<ModelElement> getMembers();

    static Scope extendScope(@Nullable Scope actual_self, ModelElement modelElement, List<ModelElement> members)
    {
        return new ScopeImpl(actual_self, modelElement, members);
    }
}

class ScopeImpl implements Scope
{
    private static Membership membership;

    private final @Nullable Scope actual_self;

    private final ModelElement modelElement;

    ScopeImpl(@Nullable Scope actual_self, @Nullable Scope parent, @Nullable Location location, List<ModelElement> members)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.modelElement = ModelElement.extendModelElement(this.actual_self, parent, location);

        membership.linkMany(this.actual_self, members);
    }

    ScopeImpl(@Nullable Scope actual_self, ModelElement modelElement, List<ModelElement> members)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.modelElement = modelElement;

        membership.linkMany(this.actual_self, members);
    }

    public List<ModelElement> getMembers()
    {
        return membership.membersOf(actual_self);
    }

    public Optional<Scope> getParent()
    {
        return this.modelElement.getParent();
    }

    public Optional<Location> getLocation()
    {
        return this.modelElement.getLocation();
    }

    public String toString()
    {
        return new StringBuilder(Scope.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }

    static void setMembership(Membership association)
    {
        membership = association;
    }

    static
    {
        Membership.init(Scope.class);
    }
}