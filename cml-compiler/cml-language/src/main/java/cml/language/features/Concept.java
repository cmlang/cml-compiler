package cml.language.features;

import cml.language.foundation.*;
import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.NamedElement;
import cml.language.generated.Scope;
import cml.language.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static cml.language.functions.ConceptFunctions.conceptRedefined;
import static cml.language.functions.ConceptFunctions.overridden;
import static cml.language.functions.ConceptFunctions.redefinedAncestors;
import static cml.language.functions.ModelElementFunctions.moduleOf;
import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.jooq.lambda.Seq.seq;

public interface Concept extends NamedElement, PropertyList
{
    boolean isAbstract();

    default List<String> getDependencies()
    {
        return concat(
            getGeneralizationDependencies().stream(),
            getPropertyDependencies().stream())
            .distinct()
            .collect(toList());
    }

    default List<String> getGeneralizationDependencies()
    {
        return getAllAncestors().stream()
                                .map(Concept::getName)
                                .filter(name -> !name.equals(getName()))
                                .distinct()
                                .collect(toList());
    }

    default List<String> getPropertyDependencies()
    {
        final Stream<String> generalizations = getTransitivePropertyConcepts().stream()
                                                                              .flatMap(concept -> concept.getGeneralizationDependencies().stream());

        final Stream<String> directDependencies = getTransitivePropertyConcepts().stream()
                                                                                 .map(Concept::getName);

        return concat(generalizations, directDependencies)
            .filter(name -> !name.equals(getName()))
            .distinct()
            .collect(toList());
    }

    default List<Concept> getTransitivePropertyConcepts()
    {
        final List<Concept> concepts = new ArrayList<>();

        getPropertyConcepts().forEach(c -> c.appendToPropertyConcepts(concepts));

        return concepts;
    }

    default void appendToPropertyConcepts(List<Concept> concepts)
    {
        if (!concepts.contains(this))
        {
            concepts.add(this);

            getPropertyConcepts().forEach(c -> c.appendToPropertyConcepts(concepts));
        }
    }

    default List<Concept> getPropertyConcepts()
    {
        return getPropertyTypes().stream()
                                 .filter(type -> !type.isPrimitive())
                                 .map(Type::getConcept)
                                 .filter(Optional::isPresent)
                                 .map(Optional::get)
                                 .distinct()
                                 .collect(toList());
    }

    default List<Type> getPropertyTypes()
    {
        return getAllProperties().stream()
                                 .map(Property::getType)
                                 .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<ConceptRedef> getRedefinedAncestors()
    {
        return redefinedAncestors(this, this);
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

    @SuppressWarnings("unused")
    default List<Property> getRedefinedInheritedConcreteProperties()
    {
        return getInheritedProperties()
            .stream()
            .filter(Property::isConcrete)
            .map(p -> getProperty(p.getName()).orElse(p))
            .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<Property> getSuperProperties()
    {
        return getDelegatedProperties()
            .stream()
            .filter(p -> !p.isDerived())
            .collect(toList());
    }

    default List<Property> getDelegatedProperties()
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
                                    .count() == 0;
    }

    default List<Property> getAllProperties()
    {
        return concat(
            getProperties().stream(),
            getDelegatedProperties().stream()
        ).collect(toList());
    }

    @SuppressWarnings("unused")
    default List<Property> getInitProperties()
    {
        return getAllProperties().stream()
                                 .filter(Property::isInit)
                                 .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<Property> getNonInitProperties()
    {
        return getAllProperties().stream()
                                 .filter(p -> !p.getValue().isPresent())
                                 .collect(toList());
    }

    default List<Property> getAssociationProperties()
    {
        return getProperties().stream()
                              .filter(p -> p.getAssociation().isPresent())
                              .collect(toList());
    }

    default List<Property> getSlotProperties()
    {
        return getProperties().stream()
                              .filter(Property::isSlot)
                              .collect(toList());
    }

    default List<Property> getPrintableProperties()
    {
        return getAllProperties().stream()
                                 .filter(property -> (property.isSlot() && !property.getType().isSequence()) || property.getType().isPrimitive())
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

    default Model getModel()
    {
        assert moduleOf(this).isPresent();

        return moduleOf(this).get().getModel();
    }

    default List<Association> getAssociations()
    {
        return getModel().getAssociations()
                         .stream()
                         .filter(assoc -> assoc.getAssociationEnds()
                                               .stream()
                                               .anyMatch(end -> end.getConcept().isPresent() && end.getConcept().get() == this))
                         .collect(toList());
    }

    static Concept create(Module module, String name)
    {
        return create(module, name, false, emptyList(), null);
    }

    static Concept create(Module module, String name, List<Property> propertyList)
    {
        return new ConceptImpl(module, name, false, propertyList, null);
    }

    static Concept create(Module module, String name, boolean _abstract, List<Property> propertyList, Location location)
    {
        return new ConceptImpl(module, name, _abstract, propertyList, location);
    }

    static InvariantValidator<Concept> invariantValidator()
    {
        return () -> asList(
            new NotOwnGeneralization(),
            new CompatibleGeneralizations(),
            new ConflictRedefinition(),
            new AbstractPropertyRedefinition()
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

    ConceptImpl(Module module, String name, boolean _abstract, final List<Property> propertyList, Location location)
    {
        this.modelElement = extendModelElement(this, module, location);
        this.namedElement = extendNamedElement(modelElement, name);
        this.scope = extendScope(this, modelElement, seq(propertyList).map(p -> (ModelElement)p).toList());
        this._abstract = _abstract;
    }

    @Override
    public boolean isAbstract()
    {
        return _abstract;
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return modelElement.getParent();
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
                   .map(Pair::getLeft)
                   .allMatch(propertyRedefinedIn(self));
    }

    private Stream<Pair<Property>> getConflictingPropertyPairs(Concept self)
    {
        return self.getGeneralizationPropertyPairs().stream()
                   .filter(pair -> pair.getLeft().matchesTypeOf(pair.getRight()))
                   .filter(pair -> pair.getLeft().isDerived() ||
                                   pair.getLeft().getValue().isPresent() ||
                                   pair.getRight().isDerived() ||
                                   pair.getRight().getValue().isPresent());
    }

    private Predicate<Property> propertyRedefinedIn(Concept self)
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

class AbstractPropertyRedefinition implements Invariant<Concept>
{
    @Override
    public boolean evaluate(Concept self)
    {
        return self.isAbstract() || getInheritedAbstractProperties(self).allMatch(abstractPropertyRedefinedIn(self));
    }

    private Predicate<Property> abstractPropertyRedefinedIn(Concept self)
    {
        return p1 -> self.getProperties()
                         .stream()
                         .filter(p2 -> p1.getName().equals(p2.getName()))
                         .filter(Property::isConcrete)
                         .count() > 0;
    }

    @Override
    public Diagnostic createDiagnostic(Concept self)
    {
        final List<Property> abstractProperties = getInheritedAbstractProperties(self)
                                                      .filter(abstractPropertyRedefinedIn(self).negate())
                                                      .collect(toList());

        return new Diagnostic("abstract_property_redefinition", self, abstractProperties);
    }

    private Stream<Property> getInheritedAbstractProperties(Concept self)
    {
        return self.getDirectAncestors()
                   .stream()
                   .flatMap(c -> c.getAllProperties().stream())
                   .filter(Property::isAbstract);
    }
}