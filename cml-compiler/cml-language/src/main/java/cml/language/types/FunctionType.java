package cml.language.types;

import cml.language.foundation.ModelElementBase;
import org.jooq.lambda.Seq;

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

    public Seq<Type> getParamTypes()
    {
        return params.getElements().map(TupleTypeElement::getType);
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
    public Type getElementType()
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
