package cml.language.features;

import cml.language.foundation.*;
import cml.language.generated.Location;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface Task extends NamedElement, PropertyList
{
    Optional<String> getConstructor();
    void setConstructor(String constructor);

    default Optional<Module> getModule()
    {
        return getParentScope().map(scope -> (Module) scope);
    }

    static Task create(String name)
    {
        return new TaskImpl(name);
    }
}

class TaskImpl implements Task
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    private @Nullable String constructor;

    TaskImpl(String name)
    {
        this.modelElement = ModelElement.create(this);
        this.namedElement = NamedElement.create(modelElement, name);
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
    public String getName()
    {
        return namedElement.getName();
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

    @Override
    public Optional<String> getConstructor()
    {
        return Optional.ofNullable(constructor);
    }

    @Override
    public void setConstructor(@Nullable String constructor)
    {
        this.constructor = constructor;
    }
}

