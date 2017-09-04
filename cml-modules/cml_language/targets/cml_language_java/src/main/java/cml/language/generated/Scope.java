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

    static Scope createScope(List<ModelElement> members, @Nullable Location location, @Nullable Scope parent)
    {
        ModelElement modelElement = ModelElement.extendModelElement(location, parent);
        return new ScopeImpl(modelElement, members);
    }

    static Scope extendScope(ModelElement modelElement, List<ModelElement> members)
    {
        return new ScopeImpl(modelElement, members);
    }
}

class ScopeImpl implements Scope
{
    private static Membership membership;

    private final ModelElement modelElement;

    public ScopeImpl(ModelElement modelElement, List<ModelElement> members)
    {
        this.modelElement = modelElement;

        membership.linkMany(this, members);
    }

    public List<ModelElement> getMembers()
    {
        return membership.membersOf(this);
    }

    public Optional<Location> getLocation()
    {
        return this.modelElement.getLocation();
    }

    public Optional<Scope> getParent()
    {
        return this.modelElement.getParent();
    }

    public String toString()
    {
        return new StringBuilder(Scope.class.getSimpleName())
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
        Membership.init(Scope.class);
    }
}