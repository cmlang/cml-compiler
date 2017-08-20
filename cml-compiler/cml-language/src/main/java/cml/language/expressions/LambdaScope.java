package cml.language.expressions;

import cml.language.foundation.ScopeBase;
import cml.language.types.Type;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class LambdaScope extends ScopeBase
{
    private final Map<String, Type> parameters = new HashMap<>();

    public void addParameter(final String name, final Type type)
    {
        parameters.put(name, type);
    }

    @Override
    public Optional<Type> getTypeOfVariableNamed(final String name)
    {
        return ofNullable(parameters.get(name));
    }
}
