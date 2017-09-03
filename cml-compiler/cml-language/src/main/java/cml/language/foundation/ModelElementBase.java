package cml.language.foundation;

import cml.language.generated.Location;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ModelElementBase implements ModelElement
{
    protected final ModelElement modelElement;

    protected ModelElementBase()
    {
        modelElement = ModelElement.create(this);
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public void setLocation(@Nullable Location location)
    {
        modelElement.setLocation(location);
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }
}
