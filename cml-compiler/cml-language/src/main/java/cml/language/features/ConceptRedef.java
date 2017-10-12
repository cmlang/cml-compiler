package cml.language.features;

import java.util.ArrayList;
import java.util.List;

public class ConceptRedef
{
    private final TempConcept concept;
    private final List<PropertyRedef> propertyRedefs;

    public ConceptRedef(TempConcept concept, List<PropertyRedef> properties)
    {
        this.concept = concept;
        this.propertyRedefs = new ArrayList<>(properties);
    }

    public TempConcept getConcept()
    {
        return concept;
    }

    public List<PropertyRedef> getPropertyRedefs()
    {
        return new ArrayList<>(propertyRedefs);
    }
}

