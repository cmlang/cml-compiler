package cml.language.features;

import cml.language.foundation.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface ConceptRedefined
{
    Concept getConcept();
    List<Property> getProperties();

    static ConceptRedefined create(Concept concept, List<Property> properties)
    {
        return new ConceptRedefinedImpl(concept, properties);
    }
}

class ConceptRedefinedImpl implements ConceptRedefined
{
    private final Concept concept;
    private final List<Property> properties;

    ConceptRedefinedImpl(Concept concept, List<Property> properties)
    {
        this.concept = concept;
        this.properties = new ArrayList<>(properties);
    }

    @Override
    public Concept getConcept()
    {
        return concept;
    }

    @Override
    public List<Property> getProperties()
    {
        return new ArrayList<>(properties);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConceptRedefinedImpl that = (ConceptRedefinedImpl) o;
        return Objects.equals(concept, that.concept);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(concept);
    }
}

