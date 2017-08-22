package cml.language.foundation;

import cml.language.features.Association;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.features.Task;
import cml.language.loader.ModelVisitor;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface Model extends NamedElement, Scope
{
    default String getName()
    {
        return "model";
    }

    default List<Module> getModules()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Module)
                           .map(e -> (Module)e)
                           .collect(toList());
    }

    default Optional<Module> getModule(final String name)
    {
        for (final Module module: getModules())
        {
            if (module.getName().equals(name))
            {
                return Optional.of(module);
            }
        }

        return Optional.empty();
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

    default Optional<Concept> getConcept(String name)
    {
        return getConcepts().stream()
                            .filter(concept -> concept.getName().equals(name))
                            .findFirst();
    }

    default List<Association> getAssociations()
    {
        return getModules().stream()
                           .flatMap(m -> m.getAssociations().stream())
                           .collect(toList());
    }

    default Optional<Association> getAssociation(String name)
    {
        return getAssociations().stream()
                                .filter(association -> association.getName().equals(name))
                                .findFirst();
    }

    default List<Task> getTasks()
    {
        return getModules().stream()
                           .flatMap(m -> m.getTasks().stream())
                           .collect(toList());
    }

    default Optional<Task> getTarget(String name)
    {
        return getTasks().stream()
                         .filter(target -> target.getName().equals(name))
                         .findFirst();
    }

    default void visit(ModelVisitor visitor)
    {
        visitor.visit(this);

        getConcepts().forEach(c -> c.visit(visitor));

        getAssociations().forEach(a -> a.visit(visitor));
    }

    static Model create()
    {
        return new ModelImpl();
    }
}

class ModelImpl implements Model
{
    private final ModelElement modelElement;
    private final Scope scope;

    ModelImpl()
    {
        this.modelElement = ModelElement.create(this);
        this.scope = Scope.create(this, modelElement);
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
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }

    @Override
    public void addMember(ModelElement member)
    {
        scope.addMember(member);
    }
}

