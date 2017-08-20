package cml.language.features;

import cml.language.types.FunctionType;
import cml.language.types.Type;
import cml.language.types.TypedElementBase;

import static java.lang.String.format;

public class FunctionParameter extends TypedElementBase
{
    public FunctionParameter(final String name, final Type type)
    {
        super(name, type);
    }

    public Type getMatchingResultType()
    {
        return getType() instanceof FunctionType ? ((FunctionType)getType()).getResult() : getType();
    }

    @Override
    public String toString()
    {
        return format("%s: %s", getName(), getType());
    }
}
