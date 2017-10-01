package cml.language.foundation;

import cml.language.generated.*;
import cml.language.types.NamedType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static cml.language.functions.ModelElementFunctions.siblingNamed;
import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static java.util.Optional.empty;

public interface Parameter extends TypedElement
{
    Optional<String> getScopeName();
    
    default Optional<Parameter> getParameterScope()
    {
        if (getScopeName().isPresent())
        {
            return siblingNamed(getScopeName().get(), this, Parameter.class);
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
        modelElement = extendModelElement(this, null, null);
        namedElement = extendNamedElement(this, modelElement, name);

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
    public Optional<Scope> getParent()
    {
        return modelElement.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return modelElement.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return modelElement.getModule();
    }
}
