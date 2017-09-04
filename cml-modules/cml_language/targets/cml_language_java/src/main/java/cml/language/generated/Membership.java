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

    private final Map<ModelElement, @Nullable Scope> scope = new HashMap<>();
    private final Map<Scope, List<ModelElement>> members = new HashMap<>();

    synchronized void linkMany(@Nullable Scope scope, List<ModelElement> members)
    {
        for (ModelElement modelElement: members) link(scope, modelElement);
    }

    synchronized void link(Scope scope, ModelElement modelElement)
    {
        this.scope.put(modelElement, scope);

        final List<ModelElement> modelElementList = this.members.computeIfAbsent(scope, key -> new ArrayList<>());
        if (!modelElementList.contains(modelElement))
        {
            modelElementList.add(modelElement);
        }
    }

    synchronized Optional<Scope> scopeOf(ModelElement modelElement)
    {
        return Optional.ofNullable(this.scope.get(modelElement));
    }

    synchronized List<ModelElement> membersOf(Scope scope)
    {
        final List<ModelElement> modelElementList = this.members.get(scope);

        return (modelElementList == null) ? Collections.emptyList() : new ArrayList<>(modelElementList);
    }
}