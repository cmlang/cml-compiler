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

public interface Concept extends NamedElement, PropertyList
{
    boolean isAbstracted();

    static Concept createConcept(String name, @Nullable ModelElement parent, List<ModelElement> elements, boolean abstracted)
    {
        return new ConceptImpl(null, name, parent, elements, abstracted);
    }

    static Concept extendConcept(@Nullable Concept actual_self, ModelElement modelElement, NamedElement namedElement, PropertyList propertyList, boolean abstracted)
    {
        return new ConceptImpl(actual_self, modelElement, namedElement, propertyList, abstracted);
    }
}

class ConceptImpl implements Concept
{
    private final @Nullable Concept actual_self;

    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final PropertyList propertyList;

    private final boolean abstracted;

    ConceptImpl(@Nullable Concept actual_self, String name, @Nullable ModelElement parent, List<ModelElement> elements, boolean abstracted)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.modelElement = ModelElement.extendModelElement(this.actual_self, parent, elements);
        this.namedElement = NamedElement.extendNamedElement(this.actual_self, this.modelElement, name);
        this.propertyList = PropertyList.extendPropertyList(this.actual_self, this.modelElement);
        this.abstracted = abstracted;
    }

    ConceptImpl(@Nullable Concept actual_self, ModelElement modelElement, NamedElement namedElement, PropertyList propertyList, boolean abstracted)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.modelElement = modelElement;
        this.namedElement = namedElement;
        this.propertyList = propertyList;
        this.abstracted = abstracted;
    }

    public boolean isAbstracted()
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
                   .append("abstracted=").append(String.format("\"%s\"", this.actual_self.isAbstracted())).append(", ")
                   .append("name=").append(String.format("\"%s\"", this.actual_self.getName())).append(", ")
                   .append("parent=").append(this.actual_self.getParent().isPresent() ? String.format("\"%s\"", this.actual_self.getParent()) : "not present")
                   .append(')')
                   .toString();
    }
}