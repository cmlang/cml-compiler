package mcml.cmlc;

import java.util.*;
import java.util.function.*;
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

    static NamedElement extendNamedElement(@Nullable NamedElement actual_self, ModelElement modelElement, String name)
    {
        return new NamedElementImpl(actual_self, modelElement, name);
    }
}

class NamedElementImpl implements NamedElement
{
    private final @Nullable NamedElement actual_self;

    private final ModelElement modelElement;

    private final String name;

    NamedElementImpl(@Nullable NamedElement actual_self, @Nullable ModelElement parent, List<ModelElement> elements, String name)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.modelElement = ModelElement.extendModelElement(this.actual_self, parent, elements);
        this.name = name;
    }

    NamedElementImpl(@Nullable NamedElement actual_self, ModelElement modelElement, String name)
    {
        this.actual_self = actual_self == null ? this : actual_self;

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
                   .append("name=").append(String.format("\"%s\"", this.actual_self.getName())).append(", ")
                   .append("parent=").append(this.actual_self.getParent().isPresent() ? String.format("\"%s\"", this.actual_self.getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}