package cml.language.types;

import cml.language.foundation.ModelElementBase;
import org.jooq.lambda.Seq;

import java.util.Optional;

import static java.lang.String.format;

public class FunctionType extends ModelElementBase implements TempType
{
    private final TupleType params;
    private final TempType result;

    public FunctionType(final TupleType params, final TempType result)
    {
        this.params = params;
        this.result = result;
    }

    public TupleType getParams()
    {
        return params;
    }

    public TempType getResult()
    {
        return result;
    }

    public Seq<TempType> getParamTypes()
    {
        return params.getElements().map(TupleTypeElement::getType);
    }

    public boolean isSingleParam()
    {
        return getParams().getElements().count() == 1;
    }

    public TempType getSingleParamType()
    {
        assert isSingleParam();

        final Optional<TupleTypeElement> single = getParams().getElements().findSingle();

        assert single.isPresent();

        return single.get().getType();
    }

    public TempType getMatchingResultType()
    {
        return getResult();
    }

    @Override
    public TempType getElementType()
    {
        assert !getCardinality().isPresent();

        return this;
    }

    @Override
    public String toString()
    {
        return format("%s -> %s", params, result);
    }
}
