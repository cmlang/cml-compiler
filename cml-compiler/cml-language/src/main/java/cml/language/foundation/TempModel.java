package cml.language.foundation;

import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.features.Template;
import cml.language.generated.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public interface TempModel extends NamedElement, Scope, Model
{
    default List<TempConcept> getOrderedConcepts()
    {
        return getConcepts().stream()
                            .map(c -> (TempConcept)c)
                            .sorted(byDependencyOrder())
                            .collect(toList());
    }

    static Comparator<TempConcept> byDependencyOrder()
    {
        return (TempConcept c1, TempConcept c2) -> {
            try
            {
                final boolean c1_gt_c2 = c1.getDependencies().contains(c2.getName());
                final boolean c2_gt_c1 = c2.getDependencies().contains(c1.getName());

                if (c1_gt_c2 && c2_gt_c1)
                {
                    // If concepts depend on each other, the more general one is listed first:
                    if (c1.getGeneralizationDependencies().contains(c2.getName())) return +1;
                    else if (c2.getGeneralizationDependencies().contains(c1.getName())) return -1;
                    else return 0;
                }
                else if (c1_gt_c2) return +1;
                else if (c2_gt_c1) return -1;
                else return 0;
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                return 0;
            }
        };
    }

    default List<Association> getAssociations()
    {
        return getModules().stream()
                           .map(m -> (TempModule)m)
                           .flatMap(m -> m.getAssociations().stream())
                           .collect(toList());
    }

    default List<Template> getTemplates()
    {
        return getModules().stream()
                           .map(m -> (TempModule)m)
                           .flatMap(m -> m.getTemplates().stream())
                           .collect(toList());
    }

    static TempModel create()
    {
        return new ModelImpl();
    }
}

class ModelImpl implements TempModel
{
    private final Model model;

    ModelImpl()
    {
        final ModelElement modelElement = extendModelElement(this, null, null);
        final NamedElement namedElement = extendNamedElement(this, modelElement, "");
        final Scope scope = extendScope(this, modelElement, emptyList());

        this.model = Model.extendModel(this, modelElement, namedElement, scope);
    }

    @Override
    public Optional<Location> getLocation()
    {
        return model.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return model.getParent();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return model.getMembers();
    }

    @Override
    public Optional<Model> getModel()
    {
        return model.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return model.getModule();
    }

    @Override
    public List<Module> getModules()
    {
        return model.getModules();
    }

    @Override
    public List<Task> getTasks()
    {
        return model.getTasks();
    }

    @Override
    public List<Concept> getConcepts()
    {
        return model.getConcepts();
    }

    @Override
    public String getName()
    {
        return model.getName();
    }
}

