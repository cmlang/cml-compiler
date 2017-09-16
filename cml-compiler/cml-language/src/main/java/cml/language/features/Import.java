package cml.language.features;

import cml.language.foundation.NamedElement;
import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.Scope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static cml.language.generated.ModelElement.extendModelElement;

public interface Import extends NamedElement
{
    Optional<Module> getImportedModule();
    void setImportedModule(@NotNull Module module);

    static Import create(Module module, String name)
    {
        return new ImportImpl(module, name);
    }
}

class ImportImpl implements Import
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;

    private @Nullable Module module;

    ImportImpl(Module module, String name)
    {
        this.modelElement = extendModelElement(this, module, null);
        this.namedElement = NamedElement.create(modelElement, name);
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
        return namedElement.getName();
    }

    @Override
    public Optional<Module> getImportedModule()
    {
        return Optional.ofNullable(module);
    }

    @Override
    public void setImportedModule(@NotNull Module module)
    {
        this.module = module;
    }
}
