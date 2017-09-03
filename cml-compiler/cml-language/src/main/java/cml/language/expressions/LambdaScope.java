package cml.language.expressions;

import cml.language.foundation.ScopeBase;
import cml.language.types.Type;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

public class LambdaScope extends ScopeBase
{
    private final Map<String, Type> parameters = new HashMap<>();

    public void addParameter(final String name, final Type type)
    {
        parameters.put(name, type);
    }

    public Map<String, Type> getParameters()
    {
        return unmodifiableMap(parameters);
    }
}
