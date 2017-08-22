package cml.language.features;

import cml.language.foundation.*;
import cml.language.loader.ModelVisitor;
import cml.language.types.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public interface AssociationEnd extends ModelElement
{
    String getConceptName();
    String getPropertyName();
    Optional<Type> getPropertyType();

    Optional<Concept> getConcept();
    void setConcept(@NotNull Concept concept);

    Optional<Property> getProperty();
    void setProperty(@NotNull Property property);

    default void visit(ModelVisitor visitor)
    {
        visitor.visit(this);
    }

    static AssociationEnd create(String conceptName, String propertyName)
    {
        return new AssociationEndImpl(conceptName, propertyName, null);
    }

    static AssociationEnd create(String conceptName, String propertyName, @Nullable Type propertyType)
    {
        return new AssociationEndImpl(conceptName, propertyName, propertyType);
    }

    static InvariantValidator<AssociationEnd> invariantValidator()
    {
        return () -> asList(
            new AssociationEndPropertyFoundInModel(),
            new AssociationEndTypeMatchesPropertyType()
        );
    }
}

class AssociationEndImpl implements AssociationEnd
{
    private final ModelElement modelElement;
    private final String conceptName;
    private final String propertyName;
    private final @Nullable Type propertyType;

    private @Nullable Concept concept;
    private @Nullable Property property;

    AssociationEndImpl(String conceptName, String propertyName, @Nullable Type propertyType)
    {
        this.modelElement = ModelElement.create(this);
        this.conceptName = conceptName;
        this.propertyName = propertyName;
        this.propertyType = propertyType;
    }

    @Override
    public String getConceptName()
    {
        return conceptName;
    }

    @Override
    public String getPropertyName()
    {
        return propertyName;
    }

    @Override
    public Optional<Type> getPropertyType()
    {
        return Optional.ofNullable(propertyType);
    }

    @Override
    public Optional<Concept> getConcept()
    {
        return Optional.ofNullable(concept);
    }

    @Override
    public void setConcept(@NotNull Concept concept)
    {
        assert this.concept == null;

        this.concept = concept;
    }

    @Override
    public Optional<Property> getProperty()
    {
        return Optional.ofNullable(property);
    }

    @Override
    public void setProperty(@NotNull Property property)
    {
        assert this.property == null;

        this.property = property;
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

class AssociationEndPropertyFoundInModel implements Invariant<AssociationEnd>
{
    @Override
    public boolean evaluate(AssociationEnd self)
    {
        return self.getConcept().isPresent() && self.getProperty().isPresent();
    }

    @Override
    public Diagnostic createDiagnostic(AssociationEnd self)
    {
        @SuppressWarnings("ConstantConditions")
        final List<ModelElement> participants = concat(of(self.getConcept()), of(self.getProperty()))
            .filter(Optional::isPresent)
            .map(e -> (ModelElement)e.get())
            .collect(toList());

        return new Diagnostic("association_end_property_found_in_model", self, participants);
    }
}

class AssociationEndTypeMatchesPropertyType implements Invariant<AssociationEnd>
{
    @Override
    public boolean evaluate(AssociationEnd self)
    {
        return !self.getPropertyType().isPresent() ||
               !self.getProperty().isPresent() ||
               self.getPropertyType().get().equals(self.getProperty().get().getType());
    }

    @Override
    public Diagnostic createDiagnostic(AssociationEnd self)
    {
        assert self.getProperty().isPresent();

        return new Diagnostic(
            "association_end_type_matches_property_type",
            self,
            singletonList(self.getProperty().get()));
    }
}