package cml.language.types;

import cml.language.foundation.ModelElementBase;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import sun.reflect.generics.tree.TypeTree;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.jooq.lambda.Seq.seq;
import static org.jooq.lambda.Seq.zip;

public class TupleType extends ModelElementBase implements Type
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

    public Seq<Type> getElementTypes()
    {
        return seq(elements).map(TupleTypeElement::getType);
    }

    @Override
    public Optional<String> getCardinality()
    {
        return Optional.ofNullable(cardinality);
    }

    @Override
    public Type withCardinality(final String cardinality)
    {
        final TupleType tupleType = new TupleType(seq(elements), cardinality);

        getConcept().ifPresent(tupleType::setConcept);

        return tupleType;
    }

    @Override
    public Type getElementType()
    {
        final TupleType tupleType = new TupleType(seq(elements), null);

        getConcept().ifPresent(tupleType::setConcept);

        return tupleType;
    }

    @Override
    public boolean isElementTypeAssignableFrom(final Type otherElementType)
    {
        if (this.getCardinality().isPresent())
        {
            System.err.println("TupleType: " + this);
        }

        assert !this.getCardinality().isPresent();
        assert !otherElementType.getCardinality().isPresent();

        if (otherElementType instanceof TupleType)
        {
            final TupleType other = (TupleType)otherElementType;

            return zip(this.elements, other.elements).allMatch(t -> t.v1.isAssignableFrom(t.v2));
        }

        return false;
    }

    @Override
    public String toString()
    {
        return format("(%s)", seq(elements).toString(",")) + (getCardinality().isPresent() ? getCardinality().get() : "");
    }
}
