package cml.language.features;

import cml.language.generated.Type;
import cml.language.types.TypedElementBase;

import static cml.language.functions.ModelElementFunctions.diagnosticIdentificationOf;
import static java.lang.String.format;

public class FunctionParameter extends TypedElementBase
{
    public FunctionParameter(final String name, final Type type)
    {
        super(name, type);
    }

    public Type getMatchingResultType()
    {
        return getType().getMatchingResultType();
    }

    @Override
    public String toString()
    {
        return format("%s: %s", getName(), diagnosticIdentificationOf(getType()));
    }
}
