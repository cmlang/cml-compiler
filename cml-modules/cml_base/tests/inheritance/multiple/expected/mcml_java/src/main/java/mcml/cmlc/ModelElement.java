package mcml.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface ModelElement
{
    Optional<ModelElement> getParent();

    List<ModelElement> getElements();

    static ModelElement extendModelElement(@Nullable ModelElement parent, List<ModelElement> elements)
    {
        return new ModelElementImpl(parent, elements);
    }
}

class ModelElementImpl implements ModelElement
{
    private final @Nullable ModelElement parent;
    private final List<ModelElement> elements;

    ModelElementImpl(@Nullable ModelElement parent, List<ModelElement> elements)
    {
        this.parent = parent;
        this.elements = elements;
    }

    public Optional<ModelElement> getParent()
    {
        return Optional.ofNullable(this.parent);
    }

    public List<ModelElement> getElements()
    {
        return Collections.unmodifiableList(this.elements);
    }

    public String toString()
    {
        return new StringBuilder(ModelElement.class.getSimpleName())
                   .append('(')
                   .append("parent=").append(getParent().isPresent() ? String.format("\"%s\"", getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}