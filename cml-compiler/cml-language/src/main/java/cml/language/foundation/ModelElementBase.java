package cml.language.foundation;

import cml.language.generated.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static cml.language.generated.Element.extendElement;
import static cml.language.generated.ModelElement.extendModelElement;

public abstract class ModelElementBase implements ModelElement
{
    protected final Element element;
    protected final ModelElement modelElement;

    protected ModelElementBase()
    {
        this(null);
    }

    protected ModelElementBase(@Nullable Scope parent)
    {
        element = extendElement(this);
        modelElement = extendModelElement(this, element, parent, null);
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
    public Optional<Model> getModel()
    {
        return modelElement.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return modelElement.getModule();
    }
}
