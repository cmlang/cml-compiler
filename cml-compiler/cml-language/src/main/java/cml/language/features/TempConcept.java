package cml.language.features;

import cml.language.foundation.TempModel;
import cml.language.generated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static cml.language.functions.ConceptFunctions.redefinedAncestors;
import static cml.language.functions.ConceptFunctions.redefinedInheritedConcreteProperties;
import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.jooq.lambda.Seq.seq;

public interface TempConcept extends Concept, PropertyList
{
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
                                 .map(Type::getConcept)
                                 .filter(Optional::isPresent)
                                 .map(Optional::get)
                                 .map(c -> (TempConcept)c)
                                 .distinct()
                                 .collect(toList());
    }

    @SuppressWarnings("unused")
    default List<ConceptRedef> getRedefinedAncestors()
    {
        return redefinedAncestors(this, this);
    }

    @SuppressWarnings("unused")
    default List<Property> getRedefinedInheritedConcreteProperties()
    {
        return redefinedInheritedConcreteProperties(this);
    }

    default List<Property> getAssociationProperties()
    {
        return getProperties().stream()
                              .filter(p -> p.getAssociation().isPresent())
                              .collect(toList());
    }

    default List<Property> getPrintableProperties()
    {
        return getAllProperties().stream()
                                 .filter(property -> (property.isSlot() && !property.getType().isSequence()) || property.getType().isPrimitive())
                                 .collect(toList());
    }

    default List<TempConcept> getAllGeneralizations()
    {
        final List<TempConcept> generalizations = new ArrayList<>();

        getAncestors().forEach(c -> ((TempConcept) c).appendToGeneralizations(generalizations));

        return generalizations;
    }

    default void appendToGeneralizations(List<TempConcept> generalizations)
    {
        if (!generalizations.contains(this))
        {
            generalizations.add(this);

            getAncestors().forEach(c -> ((TempConcept) c).appendToGeneralizations(generalizations));
        }
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

    static TempConcept create(TempModule module, String name, List<Property> propertyList)
    {
        return new ConceptImpl(module, name, false, propertyList, null);
    }

    static TempConcept create(TempModule module, String name, boolean _abstract, List<Property> propertyList, Location location)
    {
        return new ConceptImpl(module, name, _abstract, propertyList, location);
    }
}

class ConceptImpl implements TempConcept
{
    private final Concept concept;

    ConceptImpl(TempModule module, String name, boolean abstraction, final List<Property> properties, Location location)
    {
        final ModelElement modelElement = extendModelElement(this, module, location);
        final NamedElement namedElement = extendNamedElement(this, modelElement, name);
        final Scope scope = extendScope(this, modelElement, seq(properties).map(p -> (ModelElement)p).toList());
        final PropertyList propertyList = PropertyList.extendPropertyList(this, modelElement, scope);
        this.concept = Concept.extendConcept(this, modelElement, scope, namedElement, propertyList, abstraction, emptyList(), emptyList());
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
    public boolean isAbstraction()
    {
        return concept.isAbstraction();
    }

    @Override
    public List<Property> getDerivedProperties()
    {
        return concept.getDerivedProperties();
    }

    @Override
    public List<Type> getPropertyTypes()
    {
        return concept.getPropertyTypes();
    }

    @Override
    public List<Property> getInitProperties()
    {
        return concept.getInitProperties();
    }

    @Override
    public List<Property> getNonInitProperties()
    {
        return concept.getNonInitProperties();
    }

    @Override
    public List<Property> getNonDerivedProperties()
    {
        return concept.getNonDerivedProperties();
    }

    @Override
    public List<Property> getInvocationProperties()
    {
        return concept.getInvocationProperties();
    }

    @Override
    public List<Property> getSlotProperties()
    {
        return concept.getSlotProperties();
    }

    @Override
    public List<Inheritance> getGeneralizations()
    {
        return concept.getGeneralizations();
    }

    @Override
    public List<Inheritance> getSpecializations()
    {
        return concept.getSpecializations();
    }

    @Override
    public List<Concept> getAncestors()
    {
        return concept.getAncestors();
    }

    @Override
    public List<Concept> getDescendants()
    {
        return concept.getDescendants();
    }

    @Override
    public List<Concept> getAllAncestors()
    {
        return concept.getAllAncestors();
    }

    @Override
    public List<Property> getAllProperties()
    {
        return concept.getAllProperties();
    }

    @Override
    public List<Concept> getInheritedAncestors()
    {
        return concept.getInheritedAncestors();
    }

    @Override
    public List<Property> getInheritedProperties()
    {
        return concept.getInheritedProperties();
    }

    @Override
    public List<Property> getInheritedAbstractProperties()
    {
        return concept.getInheritedAbstractProperties();
    }

    @Override
    public List<Property> getInheritedNonRedefinedProperties()
    {
        return concept.getInheritedNonRedefinedProperties();
    }

    @Override
    public List<Property> getSuperProperties()
    {
        return concept.getSuperProperties();
    }

    @Override
    public String toString()
    {
        return "concept " + getName();
    }
}
