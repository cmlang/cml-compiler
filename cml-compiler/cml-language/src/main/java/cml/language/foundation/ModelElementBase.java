package cml.language.foundation;

import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.Scope;
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
}
