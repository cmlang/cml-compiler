package cml.language.features;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.foundation.InvariantValidator;
import cml.language.foundation.TempProperty;
import cml.language.generated.*;
import cml.language.types.TempType;

import java.util.List;
import java.util.Optional;

import static cml.language.functions.ModelElementFunctions.selfTypeOf;
import static cml.language.functions.TypeFunctions.isEqualTo;
import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;

public interface TempAssociation extends Association
{
    default Optional<TempAssociationEnd> getAssociationEnd(String conceptName, String propertyName)
    {
        return getAssociationEnds().stream()
                                   .filter(associationEnd -> associationEnd.getConceptName().equals(conceptName))
                                   .filter(associationEnd -> associationEnd.getPropertyName().equals(propertyName))
                                   .findFirst();
    }

    default List<TempAssociationEnd> getAssociationEnds()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof TempAssociationEnd)
                           .map(e -> (TempAssociationEnd)e)
                           .collect(toList());
    }

    default List<TempType> getPropertyTypes()
    {
        return getAssociationEnds()
            .stream()
            .map(TempAssociationEnd::getProperty)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(TempProperty::getType)
            .map(t -> (TempType)t)
            .collect(toList());
    }

    default List<TempType> getReversedPropertyTypes()
    {
        return seq(getPropertyTypes()).reverse().toList();
    }

    @SuppressWarnings("unused")
    default boolean isOneToMany()
    {
        return oneToMany(getAssociationEnds().get(0), getAssociationEnds().get(1)) ||
               oneToMany(getAssociationEnds().get(1), getAssociationEnds().get(0));
    }

    @SuppressWarnings("unused")
    default boolean isOneToOne()
    {
        return oneToOne(getAssociationEnds().get(0), getAssociationEnds().get(1));
    }

    default Optional<TempProperty> getOneProperty()
    {
        if (oneToMany(getAssociationEnds().get(0), getAssociationEnds().get(1)))
        {
            return getAssociationEnds().get(0).getProperty();
        }

        if (oneToMany(getAssociationEnds().get(1), getAssociationEnds().get(0)))
        {
            return getAssociationEnds().get(1).getProperty();
        }

        return Optional.empty();
    }

    default Optional<TempProperty> getManyProperty()
    {
        if (oneToMany(getAssociationEnds().get(0), getAssociationEnds().get(1)))
        {
            return getAssociationEnds().get(1).getProperty();
        }

        if (oneToMany(getAssociationEnds().get(1), getAssociationEnds().get(0)))
        {
            return getAssociationEnds().get(0).getProperty();
        }

        return Optional.empty();
    }

    static boolean oneToMany(TempAssociationEnd end1, TempAssociationEnd end2)
    {
        assert end1.getProperty().isPresent();
        assert end2.getProperty().isPresent();

        return !end1.getProperty().get().getType().isSequence() && end2.getProperty().get().getType().isSequence();
    }

    static boolean oneToOne(TempAssociationEnd end1, TempAssociationEnd end2)
    {
        assert end1.getProperty().isPresent();
        assert end2.getProperty().isPresent();

        return end1.getProperty().get().getType().isSingle() && end2.getProperty().get().getType().isSingle();
    }

    static TempAssociation create(TempModule module, String name, List<TempAssociationEnd> associationEnds, Location location)
    {
        return new AssociationImpl(module, name, associationEnds, location);
    }

    static InvariantValidator<TempAssociation> invariantValidator()
    {
        return () -> asList(
            new AssociationMustHaveTwoAssociationEnds(),
            new AssociationEndTypesMustMatch()
        );
    }

}

class AssociationImpl implements TempAssociation
{
    private final Association association;

    AssociationImpl(TempModule module, String name, List<TempAssociationEnd> associationEnds, Location location)
    {
        final ModelElement modelElement = extendModelElement(this, module, location);
        final NamedElement namedElement = extendNamedElement(this, modelElement, name);
        final Scope scope = extendScope(this, modelElement, seq(associationEnds).map(end -> (ModelElement)end).toList());
        this.association = Association.extendAssociation(this, modelElement, namedElement, scope);
    }

    @Override
    public Optional<Location> getLocation()
    {
        return association.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return association.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return association.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return association.getModule();
    }

    @Override
    public String getName()
    {
        return association.getName();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return association.getMembers();
    }

    @Override
    public String toString()
    {
        return "association " + getName();
    }
}

class AssociationMustHaveTwoAssociationEnds implements Invariant<TempAssociation>
{
    @Override
    public boolean evaluate(TempAssociation self)
    {
        return self.getAssociationEnds().size() == 2;
    }

    @Override
    public Diagnostic createDiagnostic(TempAssociation self)
    {
        return new Diagnostic("association_must_have_two_association_ends", self, self.getAssociationEnds());
    }
}

class AssociationEndTypesMustMatch implements Invariant<TempAssociation>
{
    @Override
    public boolean evaluate(TempAssociation self)
    {
        if (self.getAssociationEnds().size() != 2)
        {
            return true;
        }

        final Optional<TempAssociationEnd> first = self.getAssociationEnds().stream().findFirst();
        final Optional<TempAssociationEnd> last = self.getAssociationEnds().stream().reduce((previous, next) -> next);

        if (!first.isPresent() || !last.isPresent())
        {
            return true;
        }

        final TempAssociationEnd end1 = first.get();
        final TempAssociationEnd end2 = last.get();

        if (!end1.getConcept().isPresent() || !end1.getProperty().isPresent() ||
            !end2.getConcept().isPresent() || !end2.getProperty().isPresent())
        {
            return true;
        }

        final TempConcept firstConcept = end1.getConcept().get();
        final TempConcept secondConcept = end2.getConcept().get();
        final TempProperty firstProperty = end1.getProperty().get();
        final TempProperty secondProperty = end2.getProperty().get();

        return typesMatch(firstConcept, secondProperty) && typesMatch(secondConcept, firstProperty);
    }

    private static boolean typesMatch(TempConcept concept, TempProperty property)
    {
        return isEqualTo(property.getType().getElementType(), selfTypeOf(concept));
    }

    @Override
    public Diagnostic createDiagnostic(TempAssociation self)
    {
        return new Diagnostic("association_end_types_must_match", self, self.getAssociationEnds());
    }
}
