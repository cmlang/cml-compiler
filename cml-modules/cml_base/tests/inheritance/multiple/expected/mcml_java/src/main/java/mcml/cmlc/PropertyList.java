package mcml.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface PropertyList extends ModelElement
{
    static PropertyList extendPropertyList(ModelElement modelElement)
    {
        return new PropertyListImpl(modelElement);
    }
}

class PropertyListImpl implements PropertyList
{
    private final ModelElement modelElement;

    PropertyListImpl(@Nullable ModelElement parent, List<ModelElement> elements)
    {
        this.modelElement = ModelElement.extendModelElement(parent, elements);

    }

    PropertyListImpl(ModelElement modelElement)
    {
        this.modelElement = modelElement;
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
        return new StringBuilder(PropertyList.class.getSimpleName())
                   .append('(')
                   .append("parent=").append(getParent().isPresent() ? String.format("\"%s\"", getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}