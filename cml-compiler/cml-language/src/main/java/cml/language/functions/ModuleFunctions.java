package cml.language.functions;

import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.features.Template;

import java.util.Optional;

@SuppressWarnings("unused")
public class ModuleFunctions
{
    public static Optional<Module> importedModuleOf(Module module, String name)
    {
        for (final Module m: module.getImportedModules())
        {
            if (m.getName().equals(name))
            {
                return Optional.of(m);
            }
        }

        return Optional.empty();
    }

    public static Optional<Module> selfOrImportedModuleOf(Module module, String name)
    {
        if (module.getName().equals(name))
        {
            return Optional.of(module);
        }

        return importedModuleOf(module, name);
    }

    public static Optional<Concept> conceptOf(Module module, String name)
    {
        return module.getAllConcepts()
                     .stream()
                     .filter(concept -> concept.getName().equals(name))
                     .findFirst();
    }

    public static Optional<Template> templateOf(Module module, String name)
    {
        return module.getAllTemplates()
                     .stream()
                     .filter(t -> t.getName().equals(name))
                     .findFirst();
    }
}
