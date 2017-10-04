package cml.language.functions;

import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.features.Template;
import cml.language.foundation.TempModel;
import cml.language.generated.Association;
import cml.language.generated.Module;
import cml.language.generated.Task;

import java.util.Optional;

@SuppressWarnings("unused")
public class ModelFunctions
{
    public static Optional<TempModule> moduleOf(TempModel model, String name)
    {
        for (final Module module : model.getModules())
        {
            if (module.getName().equals(name))
            {
                return Optional.of((TempModule) module);
            }
        }

        return Optional.empty();
    }

    public static Optional<TempConcept> conceptOf(TempModel model, String name)
    {
        return model.getConcepts()
                    .stream()
                    .filter(concept -> concept.getName().equals(name))
                    .findFirst();
    }

    public static Optional<Association> associationOf(TempModel model, String name)
    {
        return model.getAssociations()
                    .stream()
                    .filter(association -> association.getName().equals(name))
                    .findFirst();
    }

    public static Optional<Task> targetOf(TempModel model, String name)
    {
        return model.getTasks()
                    .stream()
                    .filter(target -> target.getName().equals(name))
                    .findFirst();
    }

    public static Optional<Template> templateOf(TempModel model, String name)
    {
        return model.getTemplates()
                    .stream()
                    .filter(t -> t.getName().equals(name))
                    .findFirst();
    }
}
