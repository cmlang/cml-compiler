package cml.language.features;

import cml.language.types.Type;
import cml.language.types.TypedElementBase;

public class FunctionParameter extends TypedElementBase
{
    public FunctionParameter(final String name, final Type type)
    {
        super(name, type);
    }
}
