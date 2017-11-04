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

public interface ModelElement
{
    Optional<ModelElement> getParent();

    List<ModelElement> getElements();

    static ModelElement extendModelElement(@Nullable ModelElement actual_self, @Nullable ModelElement parent, List<ModelElement> elements)
    {
        return new ModelElementImpl(actual_self, parent, elements);
    }
}

class ModelElementImpl implements ModelElement
{
    private final @Nullable ModelElement actual_self;

    private final @Nullable ModelElement parent;
    private final List<ModelElement> elements;

    ModelElementImpl(@Nullable ModelElement actual_self, @Nullable ModelElement parent, List<ModelElement> elements)
    {
        this.actual_self = actual_self == null ? this : actual_self;

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
                   .append("parent=").append(this.actual_self.getParent().isPresent() ? String.format("\"%s\"", this.actual_self.getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}