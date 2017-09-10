package mcml.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Model extends ModelElement
{
    static Model createModel(@Nullable ModelElement parent, List<ModelElement> elements)
    {
        return new ModelImpl(parent, elements);
    }

    static Model extendModel(ModelElement modelElement)
    {
        return new ModelImpl(modelElement);
    }
}

class ModelImpl implements Model
{
    private final ModelElement modelElement;

    ModelImpl(@Nullable ModelElement parent, List<ModelElement> elements)
    {
        this.modelElement = ModelElement.extendModelElement(parent, elements);

    }

    ModelImpl(ModelElement modelElement)
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
        return new StringBuilder(Model.class.getSimpleName())
                   .append('(')
                   .append("parent=").append(getParent().isPresent() ? String.format("\"%s\"", getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}