package cml.language.features;

import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.NamedElement;
import cml.language.generated.Scope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;

public interface Import extends NamedElement
{
    Optional<TempModule> getImportedModule();
    void setImportedModule(@NotNull TempModule module);

    static Import create(TempModule module, String name)
    {
        return new ImportImpl(module, name);
    }
}

class ImportImpl implements Import
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;

    private @Nullable TempModule module;

    ImportImpl(TempModule module, String name)
    {
        this.modelElement = extendModelElement(this, module, null);
        this.namedElement = extendNamedElement(modelElement, name);
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
    public Optional<TempModule> getImportedModule()
    {
        return Optional.ofNullable(module);
    }

    @Override
    public void setImportedModule(@NotNull TempModule module)
    {
        this.module = module;
    }
}
