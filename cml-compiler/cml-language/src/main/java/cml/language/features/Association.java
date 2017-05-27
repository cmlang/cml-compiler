package cml.language.features;

import cml.language.foundation.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface Association extends NamedElement, Scope
{
    default Optional<AssociationEnd> getAssociationEnd(String conceptName, String propertyName)
    {
        return getAssociationEnds().stream()
                                   .filter(associationEnd -> associationEnd.getConceptName().equals(conceptName))
                                   .filter(associationEnd -> associationEnd.getPropertyName().equals(propertyName))
                                   .findFirst();
    }

    default List<AssociationEnd> getAssociationEnds()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof AssociationEnd)
                           .map(e -> (AssociationEnd)e)
                           .collect(toList());
    }

    static Association create(String name)
    {
        return new AssociationImpl(name);
    }
}

class AssociationImpl implements Association
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    AssociationImpl(String name)
    {
        this.modelElement = ModelElement.create(this);
        this.namedElement = NamedElement.create(modelElement, name);
        this.scope = Scope.create(this, modelElement);
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public void setLocation(@Nullable Location location)
    {
        modelElement.setLocation(location);
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }

    @Override
    public void addMember(ModelElement member)
    {
        scope.addMember(member);
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
