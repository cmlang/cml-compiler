package cml.language.types;

import cml.language.foundation.ModelElementBase;

import java.util.Optional;

import static java.lang.String.format;

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

    public boolean isSingleParam()
    {
        return getParams().getElements().count() == 1;
    }

    public Type getSingleParamType()
    {
        assert isSingleParam();

        final Optional<TupleTypeElement> single = getParams().getElements().findSingle();

        assert single.isPresent();

        return single.get().getType();
    }

    @Override
    public String toString()
    {
        return format("%s -> %s", params, result);
    }
}
