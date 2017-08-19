package cml.language.foundation;

import cml.language.types.NamedType;
import cml.language.types.TypedElement;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Optional.empty;

public interface Parameter extends TypedElement
{
    Optional<String> getScopeName();
    
    default Optional<Parameter> getParameterScope()
    {
        if (getScopeName().isPresent())
        {
            return getSiblingNamed(getScopeName().get(), Parameter.class);
        }
        else
        {
            return empty();
        }
    }

    static Parameter create(String name, NamedType type, String scopeName)
    {
        return new ParameterImpl(name, type, scopeName);
    }
}

class ParameterImpl implements Parameter
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;

    private final NamedType type;
    private final @Nullable String scopeName;

    ParameterImpl(String name, NamedType type, String scopeName)
    {
        modelElement = ModelElement.create(this);
        namedElement = NamedElement.create(modelElement, name);

        this.type = type;
        this.scopeName = scopeName;
    }

    @Override
    public Optional<String> getScopeName()
    {
        return Optional.ofNullable(scopeName);
    }

    @Override
    public NamedType getType()
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
}
