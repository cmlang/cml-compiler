package cml.language.functions;

import cml.language.features.*;
import cml.language.foundation.Model;

import java.util.Optional;

@SuppressWarnings("unused")
public class ModelFunctions
{
    public static Optional<Module> moduleOf(Model model, String name)
    {
        for (final Module module : model.getModules())
        {
            if (module.getName().equals(name))
            {
                return Optional.of(module);
            }
        }

        return Optional.empty();
    }

    public static Optional<Concept> conceptOf(Model model, String name)
    {
        return model.getConcepts()
                    .stream()
                    .filter(concept -> concept.getName().equals(name))
                    .findFirst();
    }

    public static Optional<Association> associationOf(Model model, String name)
    {
        return model.getAssociations()
                    .stream()
                    .filter(association -> association.getName().equals(name))
                    .findFirst();
    }

    public static Optional<Task> targetOf(Model model, String name)
    {
        return model.getTasks()
                    .stream()
                    .filter(target -> target.getName().equals(name))
                    .findFirst();
    }

    public static Optional<Template> templateOf(Model model, String name)
    {
        return model.getTemplates()
                    .stream()
                    .filter(t -> t.getName().equals(name))
                    .findFirst();
    }
}
