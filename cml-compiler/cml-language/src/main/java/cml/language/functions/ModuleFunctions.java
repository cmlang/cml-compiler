package cml.language.functions;

import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.foundation.TempModel;
import cml.language.generated.Import;
import cml.language.generated.Location;

import java.util.Optional;

import static cml.language.functions.ModelFunctions.moduleOf;
import static cml.language.generated.Import.createImport;

@SuppressWarnings("unused")
public class ModuleFunctions
{
    public static Optional<TempModule> importedModuleOf(TempModule module, String name)
    {
        for (final TempModule m: module.getImportedModules())
        {
            if (m.getName().equals(name))
            {
                return Optional.of(m);
            }
        }

        return Optional.empty();
    }

    public static Optional<TempModule> selfOrImportedModuleOf(TempModule module, String name)
    {
        if (module.getName().equals(name))
        {
            return Optional.of(module);
        }

        return importedModuleOf(module, name);
    }

    public static Optional<TempConcept> conceptOf(TempModule module, String name)
    {
        return module.getAllConcepts()
                     .stream()
                     .filter(concept -> concept.getName().equals(name))
                     .findFirst();
    }

    public static Import createImportOfModule(final String moduleName, final Location location, final TempModule importingModule)
    {
        assert importingModule.getModel().isPresent();

        final TempModel model = (TempModel) importingModule.getModel().get();
        final Optional<TempModule> existingModule = moduleOf(model, moduleName);
        final boolean firstImport = !existingModule.isPresent();
        final TempModule importedModule = firstImport ? TempModule.create(model, moduleName) : existingModule.get();

        return createImport(moduleName, importingModule, location, importedModule, firstImport);
    }
}
