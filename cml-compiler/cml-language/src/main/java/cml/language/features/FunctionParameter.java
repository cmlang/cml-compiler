package cml.language.features;

import cml.language.types.TempType;
import cml.language.types.TypedElementBase;

import static java.lang.String.format;

public class FunctionParameter extends TypedElementBase
{
    public FunctionParameter(final String name, final TempType type)
    {
        super(name, type);
    }

    public TempType getMatchingResultType()
    {
        return getType().getMatchingResultType();
    }

    @Override
    public String toString()
    {
        return format("%s: %s", getName(), getType());
    }
}
