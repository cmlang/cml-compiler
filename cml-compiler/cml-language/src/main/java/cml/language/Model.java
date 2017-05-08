package cml.language;

import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.features.Task;
import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface Model extends Scope
{
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

    default List<Concept> getConcepts()
    {
        return getModules().stream()
                           .flatMap(m -> m.getConcepts().stream())
                           .collect(toList());
    }

    default Optional<Concept> getConcept(String name)
    {
        return getConcepts().stream().filter(concept -> concept.getName().equals(name)).findFirst();
    }

    default List<Task> getTasks()
    {
        return getModules().stream()
                           .flatMap(m -> m.getTasks().stream())
                           .collect(toList());
    }

    default Optional<Task> getTarget(String name)
    {
        return getTasks().stream().filter(target -> target.getName().equals(name)).findFirst();
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

