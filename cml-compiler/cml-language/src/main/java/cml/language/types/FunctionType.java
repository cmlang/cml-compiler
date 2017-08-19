package cml.language.types;

import cml.language.foundation.ModelElementBase;

public class FunctionType extends ModelElementBase implements Type
{
    private final TupleType params;
    private final Type result;

    public FunctionType(final TupleType params, final Type result)
    {
        this.params = params;
        this.result = result;
    }

    public TupleType getParams()
    {
        return params;
    }

    public Type getResult()
    {
        return result;
    }
}
