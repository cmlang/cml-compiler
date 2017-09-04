package cml.language.generated;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

public class Membership
{
    private static final Membership singleton = new Membership();

    static void init(Class<?> cls)
    {
        if (ModelElement.class.isAssignableFrom(cls))
        {
            ModelElementImpl.setMembership(singleton);
        }
        if (Scope.class.isAssignableFrom(cls))
        {
            ScopeImpl.setMembership(singleton);
        }
    }

    private final Map<ModelElement, @Nullable Scope> parent = new HashMap<>();
    private final Map<Scope, List<ModelElement>> members = new HashMap<>();

    synchronized void linkMany(@Nullable Scope parent, List<ModelElement> members)
    {
        for (ModelElement modelElement: members) link(parent, modelElement);
    }

    synchronized void link(Scope scope, ModelElement modelElement)
    {
        this.parent.put(modelElement, scope);

        final List<ModelElement> modelElementList = this.members.computeIfAbsent(scope, key -> new ArrayList<>());
        if (!modelElementList.contains(modelElement))
        {
            modelElementList.add(modelElement);
        }
    }

    synchronized Optional<Scope> parentOf(ModelElement modelElement)
    {
        return Optional.ofNullable(this.parent.get(modelElement));
    }

    synchronized List<ModelElement> membersOf(Scope scope)
    {
        final List<ModelElement> modelElementList = this.members.get(scope);

        return (modelElementList == null) ? Collections.emptyList() : new ArrayList<>(modelElementList);
    }
}