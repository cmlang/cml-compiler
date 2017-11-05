package cml.language.features;

import cml.language.generated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static cml.language.functions.ConceptFunctions.redefinedAncestors;
import static cml.language.functions.ConceptFunctions.redefinedInheritedConcreteProperties;
import static cml.language.generated.Concept.extendConcept;
import static cml.language.generated.Element.extendElement;
import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.PropertyList.extendPropertyList;
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

        seq(getPropertyConcepts()).map(c -> (TempConcept)c).forEach(c -> c.appendToPropertyConcepts(concepts));

        return concepts;
    }

    default void appendToPropertyConcepts(List<TempConcept> concepts)
    {
        if (!concepts.contains(this))
        {
            concepts.add(this);

            seq(getPropertyConcepts()).map(c -> (TempConcept)c).forEach(c -> c.appendToPropertyConcepts(concepts));
        }
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
        final Element element = extendElement(this);
        final ModelElement modelElement = extendModelElement(this, element, module, location);
        final NamedElement namedElement = extendNamedElement(this, element, modelElement, name);
        final Scope scope = extendScope(this, element, modelElement, seq(properties).map(p -> (ModelElement)p).toList());
        final PropertyList propertyList = extendPropertyList(this, element, modelElement, scope);
        this.concept = extendConcept(this, element, modelElement, scope, namedElement, propertyList, abstraction, emptyList(), emptyList(), null);
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
    public List<Property> getAssociationProperties()
    {
        return concept.getAssociationProperties();
    }

    @Override
    public List<Type> getPropertyTypes()
    {
        return concept.getPropertyTypes();
    }

    @Override
    public List<Type> getPropertyConceptTypes()
    {
        return concept.getPropertyConceptTypes();
    }

    @Override
    public List<Association> getAssociations()
    {
        return concept.getAssociations();
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
    public List<Property> getPrintableProperties()
    {
        return concept.getPrintableProperties();
    }

    @Override
    public List<Concept> getPropertyConcepts()
    {
        return concept.getPropertyConcepts();
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
    public Optional<Binding> getBinding()
    {
        return concept.getBinding();
    }

    @Override
    public Optional<ReferenceType> getType()
    {
        return concept.getType();
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
    public String getDiagnosticId()
    {
        return concept.getDiagnosticId();
    }
}
