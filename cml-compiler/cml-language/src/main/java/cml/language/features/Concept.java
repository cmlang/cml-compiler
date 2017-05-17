package cml.language.features;

import cml.language.foundation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.counting;
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
        if (getAllGeneralizations().contains(this))
        {
            throw new IllegalStateException("Concept should not be its own generalization: " + getName());
        }

        return getDirectAncestors().stream()
                                   .flatMap(concept -> concept.getAllProperties().stream())
                                   .distinct()
                                   .collect(toList());
    }

    default List<Property> getSuperProperties()
    {
        return getInheritedProperties()
            .stream()
            .filter(nonRedefinedProperties())
            .collect(toList());
    }

    default Predicate<Property> nonRedefinedProperties()
    {
        return p1 -> getProperties().stream()
                                    .filter(p2 -> p1.getName().equals(p2.getName()))
                                    .collect(counting()) == 0;
    }

    default List<Property> getAllProperties()
    {
        return concat(
            getProperties().stream(),
            getSuperProperties().stream()
        ).collect(toList());
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

    default List<Concept> getAllGeneralizations()
    {
        final List<Concept> generalizations = new ArrayList<>();

        getDirectAncestors().forEach(c -> c.appendToGeneralizations(generalizations));

        return generalizations;
    }

    default void appendToGeneralizations(List<Concept> generalizations)
    {
        if (!generalizations.contains(this))
        {
            generalizations.add(this);

            getDirectAncestors().forEach(c -> c.appendToGeneralizations(generalizations));
        }
    }

    default List<Pair<Concept>> getGeneralizationPairs()
    {
        return getDirectAncestors().stream().flatMap(
            c1 -> getDirectAncestors().stream()
                                      .filter(c2 -> c1 != c2)
                                      .map(c2 -> new Pair<>(c1, c2))
        )
        .distinct()
        .collect(toList());
    }

    default List<Pair<Property>> getGeneralizationPropertyPairs()
    {
        return getGeneralizationPairs().stream().flatMap(pair ->
            pair.getLeft().getAllProperties().stream().flatMap(p1 ->
                pair.getRight()
                    .getAllProperties()
                    .stream()
                    .filter(p2 -> p1 != p2)
                    .filter(p2 -> p1.getName().equals(p2.getName()))
                    .map(p2 -> new Pair<>(p1, p2))
            )
        )
        .distinct()
        .collect(toList());
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
        return () -> asList(
            new NotOwnGeneralization(),
            new CompatibleGeneralizations(),
            new ConflictRedefinition()
        );
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

    @Override
    public String toString()
    {
        return "concept " + getName();
    }
}

class NotOwnGeneralization implements Invariant<Concept>
{
    @Override
    public boolean evaluate(Concept self)
    {
        return !self.getAllGeneralizations().contains(self);
    }

    @Override
    public Diagnostic createDiagnostic(Concept self)
    {
        return new Diagnostic("not_own_generalization", self);
    }

    @Override
    public boolean isCritical()
    {
        return true;
    }
}

class CompatibleGeneralizations implements Invariant<Concept>
{
    @Override
    public boolean evaluate(Concept self)
    {
        return self.getGeneralizationPropertyPairs()
                   .stream()
                   .allMatch(pair -> pair.getLeft().matchesTypeOf(pair.getRight()));
    }

    @Override
    public Diagnostic createDiagnostic(Concept self)
    {
        final List<Property> conflictingProperties = self.getGeneralizationPropertyPairs().stream()
            .filter(pair -> !pair.getLeft().matchesTypeOf(pair.getRight()))
            .flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()))
            .collect(toList());

        return new Diagnostic("compatible_generalizations", self, conflictingProperties);
    }
}

class ConflictRedefinition implements Invariant<Concept>
{
    @Override
    public boolean evaluate(Concept self)
    {
        return getConflictingPropertyPairs(self)
                   .map(pair -> pair.getLeft())
                   .allMatch(propertyRedefinedIn(self));
    }

    private Stream<Pair<Property>> getConflictingPropertyPairs(Concept self)
    {
        return self.getGeneralizationPropertyPairs().stream()
                   .filter(pair -> pair.getLeft().matchesTypeOf(pair.getRight()))
                   .filter(pair -> pair.getLeft().getValue().isPresent() || pair.getRight().getValue().isPresent());
    }

    Predicate<Property> propertyRedefinedIn(Concept self)
    {
        return p1 -> self.getProperties()
                         .stream()
                         .anyMatch(p2 -> p1.getName().equals(p2.getName()));
    }

    @Override
    public Diagnostic createDiagnostic(Concept self)
    {
        final List<Property> conflictingProperties = getConflictingPropertyPairs(self)
                                                         .flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()))
                                                         .filter(propertyRedefinedIn(self).negate())
                                                         .collect(toList());

        return new Diagnostic("conflict_redefinition", self, conflictingProperties);
    }
}