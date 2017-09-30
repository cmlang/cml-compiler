package cml.language.foundation;

import cml.language.generated.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ModelElementBase implements ModelElement
{
    protected final ModelElement modelElement;

    protected ModelElementBase()
    {
        this(null);
    }

    protected ModelElementBase(@Nullable Scope parent)
    {
        modelElement = ModelElement.extendModelElement(this, parent, null);
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
