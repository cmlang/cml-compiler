package cml.language.features;

import cml.language.foundation.Property;

import java.util.ArrayList;
import java.util.List;

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
}

