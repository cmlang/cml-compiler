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

    public Type getMatchingResultType()
    {
        return getResult();
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FunctionType that = (FunctionType) o;

        return params.equals(that.params) && result.equals(that.result);
    }

    @Override
    public int hashCode()
    {
        int result1 = params.hashCode();
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }

    @Override
    public String toString()
    {
        return format("%s -> %s", params, result);
    }
}
