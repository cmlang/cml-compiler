package cml.language.features;

import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.NamedElement;
import cml.language.foundation.Scope;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Import extends NamedElement
{
    Optional<Module> getModule();
    void setModule(Module module);

    static Import create(String name)
    {
        return new ImportImpl(name);
    }
}

class ImportImpl implements Import
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;

    private @Nullable Module module;

    ImportImpl(String name)
    {
        this.modelElement = ModelElement.create(this);
        this.namedElement = NamedElement.create(modelElement, name);
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

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public Optional<Module> getModule()
    {
        return Optional.ofNullable(module);
    }

    @Override
    public void setModule(Module module)
    {
        this.module = module;
    }
}
