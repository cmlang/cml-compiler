package cml.language.functions;

import cml.language.features.Concept;
import cml.language.features.TempModule;
import cml.language.features.Template;

import java.util.Optional;

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

    public static Optional<Concept> conceptOf(TempModule module, String name)
    {
        return module.getAllConcepts()
                     .stream()
                     .filter(concept -> concept.getName().equals(name))
                     .findFirst();
    }

    public static Optional<Template> templateOf(TempModule module, String name)
    {
        return module.getAllTemplates()
                     .stream()
                     .filter(t -> t.getName().equals(name))
                     .findFirst();
    }
}
