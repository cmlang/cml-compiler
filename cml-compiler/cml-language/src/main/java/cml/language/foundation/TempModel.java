package cml.language.foundation;

import cml.language.features.*;
import cml.language.generated.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public interface TempModel extends NamedElement, Scope, Model
{
    default List<Module> getModules()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Module)
                           .map(e -> (Module)e)
                           .collect(toList());
    }

    default List<Concept> getOrderedConcepts()
    {
        return getConcepts().stream()
                            .sorted(byDependencyOrder())
                            .collect(toList());
    }

    static Comparator<Concept> byDependencyOrder()
    {
        return (Concept c1, Concept c2) -> {
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

    default List<Concept> getConcepts()
    {
        return getModules().stream()
                           .flatMap(m -> m.getConcepts().stream())
                           .collect(toList());
    }

    default List<Association> getAssociations()
    {
        return getModules().stream()
                           .flatMap(m -> m.getAssociations().stream())
                           .collect(toList());
    }

    default List<Task> getTasks()
    {
        return getModules().stream()
                           .flatMap(m -> m.getTasks().stream())
                           .collect(toList());
    }

    default List<Template> getTemplates()
    {
        return getModules().stream()
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
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;
    private final Model model;

    ModelImpl()
    {
        this.modelElement = ModelElement.extendModelElement(this, null, null);
        this.namedElement = NamedElement.extendNamedElement(modelElement, "");
        this.scope = Scope.extendScope(this, modelElement, emptyList());
        this.model = Model.extendModel(modelElement, namedElement, scope);
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
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }

    @Override
    public Optional<Model> getModel()
    {
        return model.getModel();
    }

    @Override
    public String getName()
    {
        return model.getName();
    }
}

