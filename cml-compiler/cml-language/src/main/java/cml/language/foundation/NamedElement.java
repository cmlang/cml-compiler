package cml.language.foundation;

import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.Scope;

import java.util.Optional;

public interface NamedElement extends ModelElement
{
    String getName();

    static NamedElement create(ModelElement modelElement, String name)
    {
        return new NamedElementImpl(modelElement, name);
    }
}

class NamedElementImpl implements NamedElement
{
    private final ModelElement modelElement;
    private final String name;

    NamedElementImpl(ModelElement modelElement, String name)
    {
        this.modelElement = modelElement;
        this.name = name;
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return modelElement.getParent();
    }

    @Override
    public String getName()
    {
        return name;
    }
}

