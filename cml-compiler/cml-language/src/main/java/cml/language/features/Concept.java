package cml.language.features;

import cml.language.foundation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public interface Concept extends NamedElement, PropertyList
{
    boolean isAbstract();

    @Override
    default Type getSelfType()
    {
        return Type.create(getName(), null);
    }

    default List<String> getDependencies()
    {
        return concat(
            getAllAncestors().stream().map(Concept::getName),
            getPropertyTypes().stream().map(Type::getName))
            .filter(name -> !name.equals(getName()))
            .distinct()
            .collect(toList());
    }

    default List<Type> getPropertyTypes()
    {
        return getAllProperties().stream()
            .map(Property::getType)
            .filter(type -> !type.isPrimitive())
            .collect(toList());
    }

    default List<Concept> getAllAncestors()
    {
        final List<Concept> inheritedAncestors = getDirectAncestors().stream()
                                                                     .flatMap(concept -> concept.getAllAncestors().stream())
                                                                     .collect(toList());

        return concat(inheritedAncestors.stream(), getDirectAncestors().stream())
            .distinct()
            .collect(toList());
    }

    List<Concept> getDirectAncestors();
    void addDirectAncestor(Concept concept);

    default List<Property> getInheritedProperties()
    {
        return getAllAncestors().stream()
                                .flatMap(concept -> concept.getProperties().stream())
                                .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<Property> getInitProperties()
    {
        return getAllProperties().stream()
                                 .filter(p -> p.getValue().isPresent())
                                 .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<Property> getNonInitProperties()
    {
        return getAllProperties().stream()
                                 .filter(p -> !p.getValue().isPresent())
                                 .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<Property> getAllProperties()
    {
        return concat(getInheritedProperties().stream(), getProperties().stream()).collect(toList());
    }

    static Concept create(String name)
    {
        return create(name, false);
    }

    static Concept create(String name, boolean _abstract)
    {
        return new ConceptImpl(name, _abstract);
    }

    static InvariantValidator<Concept> invariantValidator()
    {
        return () -> asList(new NotOwnGeneralization());
    }
}

class ConceptImpl implements Concept
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;
    private final List<Concept> directAncestors = new ArrayList<>();
    private final boolean _abstract;

    ConceptImpl(String name, boolean _abstract)
    {
        this.modelElement = ModelElement.create(this);
        this.namedElement = NamedElement.create(modelElement, name);
        this.scope = Scope.create(this, modelElement);
        this._abstract = _abstract;
    }

    @Override
    public boolean isAbstract()
    {
        return _abstract;
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
    public List<Concept> getDirectAncestors()
    {
        return unmodifiableList(directAncestors);
    }

    @Override
    public void addDirectAncestor(Concept concept)
    {
        assert !directAncestors.contains(concept);

        directAncestors.add(concept);
    }
}

class NotOwnGeneralization implements Invariant<Concept>
{
    @Override
    public boolean evaluate(Concept self)
    {
        return !self.getAllAncestors().contains(self);
    }

    @Override
    public Diagnostic createDiagnostic(Concept self)
    {
        return new Diagnostic("not_own_generalization", self);
    }
}