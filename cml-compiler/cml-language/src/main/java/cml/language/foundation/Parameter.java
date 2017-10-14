package cml.language.foundation;

import cml.language.generated.*;
import cml.language.types.TempNamedType;
import cml.language.types.TypedElementBase;
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

    static Parameter create(String name, TempNamedType type, @Nullable String scopeName)
    {
        return new ParameterImpl(name, type, scopeName);
    }
}

class ParameterImpl extends TypedElementBase implements Parameter
{
    private final @Nullable String scopeName;

    ParameterImpl(String name, TempNamedType type, @Nullable String scopeName)
    {
        super(name, type);

        this.scopeName = scopeName;
    }

    @Override
    public Optional<String> getScopeName()
    {
        return Optional.ofNullable(scopeName);
    }

    @Override
    public String getDiagnosticId()
    {
        return getType().getDiagnosticId();
    }
}
