package cml.language.generated;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

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

    NamedElementImpl(@Nullable Scope parent, @Nullable Location location, String name)
    {
        this.modelElement = ModelElement.extendModelElement(this, parent, location);
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

    public Optional<Scope> getParent()
    {
        return this.modelElement.getParent();
    }

    public Optional<Location> getLocation()
    {
        return this.modelElement.getLocation();
    }

    public String toString()
    {
        return new StringBuilder(NamedElement.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName()))
                   .append(')')
                   .toString();
    }
}