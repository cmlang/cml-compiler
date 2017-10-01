package cml.language.expressions;

import cml.language.foundation.ScopeBase;
import cml.language.generated.Scope;
import cml.language.types.TempType;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableMap;

public class LambdaScope extends ScopeBase
{
    private final Map<String, TempType> parameters = new HashMap<>();

    public LambdaScope(Scope parent, Lambda lambda)
    {
        super(parent, singletonList(lambda.getInnerExpression()));
    }

    public void addParameter(final String name, final TempType type)
    {
        parameters.put(name, type);
    }

    public Map<String, TempType> getParameters()
    {
        return unmodifiableMap(parameters);
    }
}
