package cml.language.types;

import cml.language.foundation.ModelElementBase;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.jooq.lambda.Seq.seq;

public class TupleType extends ModelElementBase implements TempType
{
    private final List<TupleTypeElement> elements;
    private final @Nullable String cardinality;

    public TupleType(final Seq<TupleTypeElement> elements, @Nullable String cardinality)
    {
        this.elements = elements.toList();
        this.cardinality = cardinality;
    }

    public Seq<TupleTypeElement> getElements()
    {
        return seq(elements);
    }

    public Seq<TempType> getElementTypes()
    {
        return seq(elements).map(TupleTypeElement::getType);
    }

    @Override
    public Optional<String> getCardinality()
    {
        return Optional.ofNullable(cardinality);
    }

    @Override
    public TempType getElementType()
    {
        final TupleType tupleType = new TupleType(seq(elements), null);

        getConcept().ifPresent(tupleType::setConcept);

        return tupleType;
    }

    @Override
    public String toString()
    {
        return format("(%s)", seq(elements).toString(",")) + (getCardinality().isPresent() ? getCardinality().get() : "");
    }
}
