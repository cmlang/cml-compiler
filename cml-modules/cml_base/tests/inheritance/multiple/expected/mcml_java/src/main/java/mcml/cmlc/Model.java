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

public interface Model extends ModelElement
{
    static Model createModel(@Nullable ModelElement parent, List<ModelElement> elements)
    {
        return new ModelImpl(null, parent, elements);
    }

    static Model extendModel(@Nullable Model actual_self, ModelElement modelElement)
    {
        return new ModelImpl(actual_self, modelElement);
    }
}

class ModelImpl implements Model
{
    private final @Nullable Model actual_self;

    private final ModelElement modelElement;

    ModelImpl(@Nullable Model actual_self, @Nullable ModelElement parent, List<ModelElement> elements)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.modelElement = ModelElement.extendModelElement(this.actual_self, parent, elements);

    }

    ModelImpl(@Nullable Model actual_self, ModelElement modelElement)
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
        return new StringBuilder(Model.class.getSimpleName())
                   .append('(')
                   .append("parent=").append(this.actual_self.getParent().isPresent() ? String.format("\"%s\"", this.actual_self.getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}