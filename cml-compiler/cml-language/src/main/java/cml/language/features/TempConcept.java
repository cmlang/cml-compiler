package cml.language.features;

import cml.language.foundation.*;
import cml.language.generated.*;
import cml.language.types.TempType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static cml.language.functions.ConceptFunctions.redefinedAncestors;
import static cml.language.functions.ConceptFunctions.redefinedInheritedConcreteProperties;
import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.jooq.lambda.Seq.seq;

public interface TempConcept extends Concept, PropertyList
{
    boolean isAbstract();

    default List<TempProperty> getNonDerivedProperties()
    {
        return getProperties().stream()
                              .map(p -> (TempProperty)p)
                              .filter(p -> !p.isDerived())
                              .sorted(byInitOrder())
                              .collect(toList());
    }

    static Comparator<TempProperty> byInitOrder()
    {
        return (TempProperty p1, TempProperty p2) -> {
            if (p1.getValue().isPresent() && !p2.getValue().isPresent()) return +1;
            else if (!p1.getValue().isPresent() && p2.getValue().isPresent()) return -1;
            else return 0;
        };
    }

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
                                .map(TempConcept::getName)
                                .filter(name -> !name.equals(getName()))
                                .distinct()
                                .collect(toList());
    }

    default List<String> getPropertyDependencies()
    {
        final Stream<String> generalizations = getTransitivePropertyConcepts().stream()
                                                                              .flatMap(concept -> concept.getGeneralizationDependencies().stream());

        final Stream<String> directDependencies = getTransitivePropertyConcepts().stream()
                                                                                 .map(TempConcept::getName);

        return concat(generalizations, directDependencies)
            .filter(name -> !name.equals(getName()))
            .distinct()
            .collect(toList());
    }

    default List<TempConcept> getTransitivePropertyConcepts()
    {
        final List<TempConcept> concepts = new ArrayList<>();

        getPropertyConcepts().forEach(c -> c.appendToPropertyConcepts(concepts));

        return concepts;
    }

    default void appendToPropertyConcepts(List<TempConcept> concepts)
    {
        if (!concepts.contains(this))
        {
            concepts.add(this);

            getPropertyConcepts().forEach(c -> c.appendToPropertyConcepts(concepts));
        }
    }

    default List<TempConcept> getPropertyConcepts()
    {
        return getPropertyTypes().stream()
                                 .filter(type -> !type.isPrimitive())
                                 .map(TempType::getConcept)
                                 .filter(Optional::isPresent)
                                 .map(Optional::get)
                                 .map(c -> (TempConcept)c)
                                 .distinct()
                                 .collect(toList());
    }

    default List<TempType> getPropertyTypes()
    {
        return getAllProperties().stream()
                                 .map(TempProperty::getType)
                                 .map(c -> (TempType)c)
                                 .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<ConceptRedef> getRedefinedAncestors()
    {
        return redefinedAncestors(this, this);
    }

    default List<TempConcept> getAllAncestors()
    {
        final List<TempConcept> inheritedAncestors = getDirectAncestors().stream()
                                                                         .flatMap(concept -> concept.getAllAncestors().stream())
                                                                         .collect(toList());

        return concat(inheritedAncestors.stream(), getDirectAncestors().stream())
            .distinct()
            .collect(toList());
    }

    List<TempConcept> getDirectAncestors();
    void addDirectAncestor(TempConcept concept);

    default List<TempProperty> getInheritedProperties()
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
    default List<TempProperty> getRedefinedInheritedConcreteProperties()
    {
        return redefinedInheritedConcreteProperties(this);
    }

    @SuppressWarnings("unused")
    default List<TempProperty> getSuperProperties()
    {
        return getDelegatedProperties()
            .stream()
            .filter(p -> !p.isDerived())
            .collect(toList());
    }

    default List<TempProperty> getDelegatedProperties()
    {
        return getInheritedProperties()
            .stream()
            .filter(nonRedefinedProperties())
            .collect(toList());
    }

    default Predicate<TempProperty> nonRedefinedProperties()
    {
        return p1 -> getProperties().stream()
                                    .filter(p2 -> p1.getName().equals(p2.getName()))
                                    .count() == 0;
    }

    default List<TempProperty> getAllProperties()
    {
        return concat(
            getProperties().stream().map(p -> (TempProperty)p),
            getDelegatedProperties().stream()
        ).collect(toList());
    }

    @SuppressWarnings("unused")
    default List<TempProperty> getInitProperties()
    {
        return getAllProperties().stream()
                                 .filter(TempProperty::isInit)
                                 .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<TempProperty> getNonInitProperties()
    {
        return getAllProperties().stream()
                                 .filter(p -> !p.getValue().isPresent())
                                 .collect(toList());
    }

    default List<TempProperty> getAssociationProperties()
    {
        return getProperties().stream()
                              .map(p -> (TempProperty)p)
                              .filter(p -> p.getAssociation().isPresent())
                              .collect(toList());
    }

    default List<TempProperty> getSlotProperties()
    {
        return getProperties().stream()
                              .map(p -> (TempProperty)p)
                              .filter(TempProperty::isSlot)
                              .collect(toList());
    }

    default List<TempProperty> getPrintableProperties()
    {
        return getAllProperties().stream()
                                 .filter(property -> (property.isSlot() && !property.getType().isSequence()) || property.getType().isPrimitive())
                                 .collect(toList());
    }

    default List<TempConcept> getAllGeneralizations()
    {
        final List<TempConcept> generalizations = new ArrayList<>();

        getDirectAncestors().forEach(c -> c.appendToGeneralizations(generalizations));

        return generalizations;
    }

    default void appendToGeneralizations(List<TempConcept> generalizations)
    {
        if (!generalizations.contains(this))
        {
            generalizations.add(this);

            getDirectAncestors().forEach(c -> c.appendToGeneralizations(generalizations));
        }
    }

    default List<Pair<TempConcept>> getGeneralizationPairs()
    {
        return getDirectAncestors().stream().flatMap(
            c1 -> getDirectAncestors().stream()
                                      .filter(c2 -> c1 != c2)
                                      .map(c2 -> new Pair<>(c1, c2))
        )
        .distinct()
        .collect(toList());
    }

    default List<Pair<TempProperty>> getGeneralizationPropertyPairs()
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

    default List<Association> getAssociations()
    {
        return seq(getModel()).flatMap(m -> seq(((TempModel) m).getAssociations()))
                         .filter(assoc -> assoc.getAssociationEnds()
                                               .stream()
                                               .anyMatch(end -> end.getAssociatedConcept().isPresent() && end.getAssociatedConcept().get() == this))
                         .collect(toList());
    }

    static TempConcept create(TempModule module, String name)
    {
        return create(module, name, false, emptyList(), null);
    }

    static TempConcept create(TempModule module, String name, List<TempProperty> propertyList)
    {
        return new ConceptImpl(module, name, false, propertyList, null);
    }

    static TempConcept create(TempModule module, String name, boolean _abstract, List<TempProperty> propertyList, Location location)
    {
        return new ConceptImpl(module, name, _abstract, propertyList, location);
    }

    static InvariantValidator<TempConcept> invariantValidator()
    {
        return () -> asList(
            new NotOwnGeneralization(),
            new CompatibleGeneralizations(),
            new ConflictRedefinition(),
            new AbstractPropertyRedefinition()
        );
    }
}

class ConceptImpl implements TempConcept
{
    private final Concept concept;
    private final List<TempConcept> directAncestors = new ArrayList<>();
    private final boolean _abstract;

    ConceptImpl(TempModule module, String name, boolean _abstract, final List<TempProperty> properties, Location location)
    {
        final ModelElement modelElement = extendModelElement(this, module, location);
        final NamedElement namedElement = extendNamedElement(this, modelElement, name);
        final Scope scope = extendScope(this, modelElement, seq(properties).map(p -> (ModelElement)p).toList());
        final PropertyList propertyList = PropertyList.extendPropertyList(this, modelElement, scope);
        this.concept = Concept.extendConcept(this, modelElement, scope, namedElement, propertyList);
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
        return concept.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return concept.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return concept.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return concept.getModule();
    }

    @Override
    public String getName()
    {
        return concept.getName();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return concept.getMembers();
    }

    @Override
    public List<Property> getProperties()
    {
        return concept.getProperties();
    }

    @Override
    public List<Property> getDerivedProperties()
    {
        return concept.getDerivedProperties();
    }

    @Override
    public List<TempConcept> getDirectAncestors()
    {
        return unmodifiableList(directAncestors);
    }

    @Override
    public void addDirectAncestor(TempConcept concept)
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

class NotOwnGeneralization implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return !self.getAllGeneralizations().contains(self);
    }

    @Override
    public Diagnostic createDiagnostic(TempConcept self)
    {
        return new Diagnostic("not_own_generalization", self);
    }

    @Override
    public boolean isCritical()
    {
        return true;
    }
}

class CompatibleGeneralizations implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return self.getGeneralizationPropertyPairs()
                   .stream()
                   .allMatch(pair -> isAssignableFrom(pair.getLeft().getType(), pair.getRight().getType()));
    }

    @Override
    public Diagnostic createDiagnostic(TempConcept self)
    {
        final List<TempProperty> conflictingProperties = self.getGeneralizationPropertyPairs().stream()
                                                             .filter(pair -> !isAssignableFrom(pair.getLeft().getType(), pair.getRight().getType()))
                                                             .flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()))
                                                             .collect(toList());

        return new Diagnostic("compatible_generalizations", self, conflictingProperties);
    }
}

class ConflictRedefinition implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return getConflictingPropertyPairs(self)
                   .map(Pair::getLeft)
                   .allMatch(propertyRedefinedIn(self));
    }

    private Stream<Pair<TempProperty>> getConflictingPropertyPairs(TempConcept self)
    {
        return self.getGeneralizationPropertyPairs().stream()
                   .filter(pair -> isAssignableFrom(pair.getLeft().getType(), pair.getRight().getType()))
                   .filter(pair -> pair.getLeft().isDerived() ||
                                   pair.getLeft().getValue().isPresent() ||
                                   pair.getRight().isDerived() ||
                                   pair.getRight().getValue().isPresent());
    }

    private Predicate<TempProperty> propertyRedefinedIn(TempConcept self)
    {
        return p1 -> self.getProperties()
                         .stream()
                         .anyMatch(p2 -> p1.getName().equals(p2.getName()));
    }

    @Override
    public Diagnostic createDiagnostic(TempConcept self)
    {
        final List<TempProperty> conflictingProperties = getConflictingPropertyPairs(self)
                                                         .flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()))
                                                         .filter(propertyRedefinedIn(self).negate())
                                                         .collect(toList());

        return new Diagnostic("conflict_redefinition", self, conflictingProperties);
    }
}

class AbstractPropertyRedefinition implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return self.isAbstract() || getInheritedAbstractProperties(self).allMatch(abstractPropertyRedefinedIn(self));
    }

    private Predicate<TempProperty> abstractPropertyRedefinedIn(TempConcept self)
    {
        return p1 -> self.getProperties()
                         .stream()
                         .filter(p2 -> p1.getName().equals(p2.getName()))
                         .map(p -> (TempProperty)p)
                         .filter(TempProperty::isConcrete)
                         .count() > 0;
    }

    @Override
    public Diagnostic createDiagnostic(TempConcept self)
    {
        final List<TempProperty> abstractProperties = getInheritedAbstractProperties(self)
                                                      .filter(abstractPropertyRedefinedIn(self).negate())
                                                      .collect(toList());

        return new Diagnostic("abstract_property_redefinition", self, abstractProperties);
    }

    private Stream<TempProperty> getInheritedAbstractProperties(TempConcept self)
    {
        return self.getDirectAncestors()
                   .stream()
                   .flatMap(c -> c.getAllProperties().stream())
                   .filter(TempProperty::isAbstract);
    }
}