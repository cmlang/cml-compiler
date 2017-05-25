package cml.language.features;

import cml.language.foundation.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface AssociationEnd extends ModelElement
{
    String getConceptName();
    String getPropertyName();
    Optional<Type> getPropertyType();

    Optional<Concept> getConcept();
    void setConcept(@Nullable Concept concept);

    Optional<Property> getProperty();
    void setProperty(@Nullable Property property);

    static AssociationEnd create(String conceptName, String propertyName, @Nullable Type propertyType)
    {
        return new AssociationEndImpl(conceptName, propertyName, propertyType);
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
    public void setConcept(@Nullable Concept concept)
    {
        this.concept = concept;
    }

    @Override
    public Optional<Property> getProperty()
    {
        return Optional.ofNullable(property);
    }

    @Override
    public void setProperty(@Nullable Property property)
    {
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
}
