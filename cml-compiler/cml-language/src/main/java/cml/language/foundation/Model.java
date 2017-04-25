package cml.language.foundation;

import cml.language.features.Concept;
import cml.language.features.Target;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface Model extends Scope
{
    default List<Concept> getConcepts()
    {
        return getElements().stream()
                            .filter(e -> e instanceof Concept)
                            .map(e -> (Concept)e)
                            .collect(toList());
    }

    default Optional<Concept> getConcept(String name)
    {
        return getConcepts().stream().filter(concept -> concept.getName().equals(name)).findFirst();
    }

    default List<Target> getTargets()
    {
        return getElements().stream()
                            .filter(e -> e instanceof Target)
                            .map(e -> (Target)e)
                            .collect(toList());
    }

    default Optional<Target> getTarget(String name)
    {
        return getTargets().stream().filter(target -> target.getName().equals(name)).findFirst();
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
    public List<ModelElement> getElements()
    {
        return scope.getElements();
    }

    @Override
    public void addElement(ModelElement element)
    {
        scope.addElement(element);
    }
}

