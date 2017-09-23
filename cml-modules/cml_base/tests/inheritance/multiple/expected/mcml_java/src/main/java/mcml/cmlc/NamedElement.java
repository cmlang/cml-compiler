package mcml.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface NamedElement extends ModelElement
{
    String getName();

    static NamedElement extendNamedElement(ModelElement modelElement, String name)
    {
        return new NamedElementImpl(modelElement, name);
    }
}

class NamedElementImpl implements NamedElement
{
    private final ModelElement modelElement;

    private final String name;

    NamedElementImpl(@Nullable ModelElement parent, List<ModelElement> elements, String name)
    {
        this.modelElement = ModelElement.extendModelElement(parent, elements);
        this.name = name;
    }

    NamedElementImpl(ModelElement modelElement, String name)
    {
        this.modelElement = modelElement;
        this.name = name;
    }

    public String getName()
    {
        return this.name;
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
        return new StringBuilder(NamedElement.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName())).append(", ")
                   .append("parent=").append(getParent().isPresent() ? String.format("\"%s\"", getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}