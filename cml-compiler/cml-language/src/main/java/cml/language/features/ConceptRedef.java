package cml.language.features;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConceptRedef
{
    private final Concept concept;
    private final List<PropertyRedef> propertyRedefs;

    ConceptRedef(Concept concept, List<PropertyRedef> properties)
    {
        this.concept = concept;
        this.propertyRedefs = new ArrayList<>(properties);
    }

    public Concept getConcept()
    {
        return concept;
    }

    public List<PropertyRedef> getPropertyRedefs()
    {
        return new ArrayList<>(propertyRedefs);
    }

    public Optional<PropertyRedef> getPropertyRedef(final PropertyRedef propertyRedef)
    {
        return propertyRedefs.stream()
                             .filter(p -> p.getProperty().getName().equals(propertyRedef.getProperty().getName()))
                             .filter(p -> p.isRedefined())
                             .findFirst();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConceptRedef that = (ConceptRedef) o;
        return Objects.equals(concept, that.concept);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(concept);
    }
}

