package mcml.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface PropertyList extends ModelElement
{
    static PropertyList extendPropertyList(@Nullable PropertyList actual_self, ModelElement modelElement)
    {
        return new PropertyListImpl(actual_self, modelElement);
    }
}

class PropertyListImpl implements PropertyList
{
    private final @Nullable PropertyList actual_self;

    private final ModelElement modelElement;

    PropertyListImpl(@Nullable PropertyList actual_self, @Nullable ModelElement parent, List<ModelElement> elements)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.modelElement = ModelElement.extendModelElement(this.actual_self, parent, elements);

    }

    PropertyListImpl(@Nullable PropertyList actual_self, ModelElement modelElement)
    {
        this.actual_self = actual_self == null ? this : actual_self;

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