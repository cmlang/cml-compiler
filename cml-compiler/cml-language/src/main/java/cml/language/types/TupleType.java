package cml.language.types;

import cml.language.generated.Type;
import org.jooq.lambda.Seq;

import java.util.List;

import static cml.language.functions.TypeFunctions.withCardinality;
import static java.lang.String.format;
import static org.jooq.lambda.Seq.seq;

public class TupleType extends BaseType
{
    private final List<TupleTypeElement> elements;

    public TupleType(final Seq<TupleTypeElement> elements)
    {
        this(elements, "");
    }

    public TupleType(final Seq<TupleTypeElement> elements, String cardinality)
    {
        super(cardinality);

        this.elements = elements.toList();
    }

    public Seq<TupleTypeElement> getElements()
    {
        return seq(elements);
    }

    public Seq<Type> getElementTypes()
    {
        return seq(elements).map(TupleTypeElement::getType);
    }

    @Override
    public Type getElementType()
    {
        return withCardinality(this, "");
    }

    @Override
    public String getDiagnosticId()
    {
        return format("(%s)", seq(elements).map(e -> e.getDiagnosticId()).toString(",")) + getCardinality();
    }
}
