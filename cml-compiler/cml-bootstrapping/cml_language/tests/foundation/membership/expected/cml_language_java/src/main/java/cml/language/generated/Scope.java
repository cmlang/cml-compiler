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

    static Scope extendScope(ModelElement modelElement, List<ModelElement> members)
    {
        return new ScopeImpl(modelElement, members);
    }
}

class ScopeImpl implements Scope
{
    private final ModelElement modelElement;

    private final List<ModelElement> members;

    ScopeImpl(@Nullable Location location, @Nullable Scope parent, List<ModelElement> members)
    {
        this.modelElement = ModelElement.extendModelElement(location, parent);
        this.members = members;
    }

    ScopeImpl(ModelElement modelElement, List<ModelElement> members)
    {
        this.modelElement = modelElement;
        this.members = members;
    }

    public List<ModelElement> getMembers()
    {
        return Collections.unmodifiableList(this.members);
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
                   .append("location=").append(getLocation().isPresent() ? String.format("\"%s\"", getLocation()) : "not present").append(", ")
                   .append("parent=").append(getParent().isPresent() ? String.format("\"%s\"", getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}