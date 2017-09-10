package mcml.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Concept extends NamedElement, PropertyList
{
    boolean getAbstracted();

    static Concept createConcept(String name, @Nullable ModelElement parent, List<ModelElement> elements, boolean abstracted)
    {
        return new ConceptImpl(name, parent, elements, abstracted);
    }

    static Concept extendConcept(ModelElement modelElement, NamedElement namedElement, PropertyList propertyList, boolean abstracted)
    {
        return new ConceptImpl(modelElement, namedElement, propertyList, abstracted);
    }
}

class ConceptImpl implements Concept
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final PropertyList propertyList;

    private final boolean abstracted;

    ConceptImpl(String name, @Nullable ModelElement parent, List<ModelElement> elements, boolean abstracted)
    {
        this.modelElement = ModelElement.extendModelElement(parent, elements);
        this.namedElement = NamedElement.extendNamedElement(modelElement, name);
        this.propertyList = PropertyList.extendPropertyList(modelElement);
        this.abstracted = abstracted;
    }

    ConceptImpl(ModelElement modelElement, NamedElement namedElement, PropertyList propertyList, boolean abstracted)
    {
        this.modelElement = modelElement;
        this.namedElement = namedElement;
        this.propertyList = propertyList;
        this.abstracted = abstracted;
    }

    public boolean getAbstracted()
    {
        return this.abstracted;
    }

    public String getName()
    {
        return this.namedElement.getName();
    }

    public Optional<ModelElement> getParent()
    {
        return this.modelElement.getParent();
    }

    public List<ModelElement> getElements()
    {
        return this.modelElement.getElements();
    }

    public String toString()
    {
        return new StringBuilder(Concept.class.getSimpleName())
                   .append('(')
                   .append("abstracted=").append(String.format("\"%s\"", getAbstracted())).append(", ")
                   .append("name=").append(String.format("\"%s\"", getName())).append(", ")
                   .append("parent=").append(getParent().isPresent() ? String.format("\"%s\"", getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}