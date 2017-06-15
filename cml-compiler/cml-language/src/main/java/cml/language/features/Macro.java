package cml.language.features;

import cml.language.foundation.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface Macro extends TypedElement, Scope
{
    default Optional<Parameter> getParameter(String name)
    {
        return getParameters().stream()
                              .filter(p -> p.getName().equals(name))
                              .findFirst();
    }

    default List<Parameter> getParameters()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Parameter)
                           .map(e -> (Parameter)e)
                           .collect(toList());
    }

    default int getParamIndexOfMatchingType()
    {
        assert getType().isParameter(): "Must be called only when macro's resulting type is a parameter.";

        int index = 0;
        for (Parameter parameter: getParameters())
        {
            if (parameter.getType().getName().equals(getType().getName()))
            {
                break;
            }
            index++;
        }

        assert index < getParameters().size(): "Expected to find a macro parameter with a type matching the resulting type.";

        return index;
    }

    static Macro create(String name, Type type)
    {
        return new MacroImpl(name, type);
    }
}

class MacroImpl implements Macro
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    private final Type type;

    MacroImpl(String name, Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.namedElement = NamedElement.create(modelElement, name);
        this.scope = Scope.create(this, modelElement);

        this.type = type;
    }

    @Override
    public Type getType()
    {
        return type;
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
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
