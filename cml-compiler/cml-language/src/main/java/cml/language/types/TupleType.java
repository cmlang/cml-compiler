package cml.language.types;

import cml.language.foundation.ModelElementBase;
import org.jooq.lambda.Seq;

import java.util.List;

import static org.jooq.lambda.Seq.seq;

public class TupleType extends ModelElementBase implements Type
{
    private final List<TupleTypeElement> elements;

    public TupleType(final Seq<TupleTypeElement> elements)
    {
        this.elements = elements.toList();
    }

    public Seq<TupleTypeElement> getElements()
    {
        return seq(elements);
    }
}
