package cml.language.features;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.foundation.InvariantValidator;
import cml.language.foundation.TempProperty;
import cml.language.generated.*;
import cml.language.types.TempType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static cml.language.functions.TypeFunctions.isEqualTo;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public interface TempAssociationEnd extends AssociationEnd
{
    Optional<TempConcept> getConcept();
    Optional<TempProperty> getProperty();

    static TempAssociationEnd create(Association association, String conceptName, String propertyName, @Nullable TempType propertyType, @Nullable TempConcept concept, @Nullable TempProperty property, Location location)
    {
        return new AssociationEndImpl(association, conceptName, propertyName, propertyType, concept, property, location);
    }

    static InvariantValidator<TempAssociationEnd> invariantValidator()
    {
        return () -> asList(
            new AssociationEndPropertyFoundInModel(),
            new AssociationEndTypeMatchesPropertyType()
        );
    }
}

class AssociationEndImpl implements TempAssociationEnd
{
    private final AssociationEnd associationEnd;

    private final @Nullable TempConcept concept;
    private final @Nullable TempProperty property;

    AssociationEndImpl(Association association, String conceptName, String propertyName, @Nullable TempType propertyType, @Nullable TempConcept concept, @Nullable TempProperty property, Location location)
    {
        final ModelElement modelElement = ModelElement.extendModelElement(this, association, location);
        associationEnd = AssociationEnd.extendAssociationEnd(this, modelElement, conceptName, propertyName, propertyType);

        this.concept = concept;
        this.property = property;
    }

    @Override
    public String getConceptName()
    {
        return associationEnd.getConceptName();
    }

    @Override
    public String getPropertyName()
    {
        return associationEnd.getPropertyName();
    }

    @Override
    public Optional<Type> getPropertyType()
    {
        return associationEnd.getPropertyType();
    }

    @Override
    public Optional<TempConcept> getConcept()
    {
        return Optional.ofNullable(concept);
    }

    @Override
    public Optional<TempProperty> getProperty()
    {
        return Optional.ofNullable(property);
    }

    @Override
    public Optional<Location> getLocation()
    {
        return associationEnd.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return associationEnd.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return associationEnd.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return associationEnd.getModule();
    }

    @Override
    public String toString()
    {
        if (getPropertyType().isPresent())
        {
            return format("association end %s.%s: %s", getConceptName(), getPropertyName(), getPropertyType().get());
        }
        else
        {
            return format("association end %s.%s", getConceptName(), getPropertyName());
        }
    }
}

class AssociationEndPropertyFoundInModel implements Invariant<TempAssociationEnd>
{
    @Override
    public boolean evaluate(TempAssociationEnd self)
    {
        return self.getConcept().isPresent() && self.getProperty().isPresent();
    }

    @Override
    public Diagnostic createDiagnostic(TempAssociationEnd self)
    {
        @SuppressWarnings("ConstantConditions")
        final List<ModelElement> participants = concat(of(self.getConcept()), of(self.getProperty()))
            .filter(Optional::isPresent)
            .map(e -> (ModelElement)e.get())
            .collect(toList());

        return new Diagnostic("association_end_property_found_in_model", self, participants);
    }
}

class AssociationEndTypeMatchesPropertyType implements Invariant<TempAssociationEnd>
{
    @Override
    public boolean evaluate(TempAssociationEnd self)
    {
        return !self.getPropertyType().isPresent() ||
               !self.getProperty().isPresent() ||
               isEqualTo(self.getPropertyType().get(), self.getProperty().get().getType());
    }

    @Override
    public Diagnostic createDiagnostic(TempAssociationEnd self)
    {
        assert self.getProperty().isPresent();

        return new Diagnostic(
            "association_end_type_matches_property_type",
            self,
            singletonList(self.getProperty().get()));
    }
}