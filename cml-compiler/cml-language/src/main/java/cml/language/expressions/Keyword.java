package cml.language.expressions;

import cml.language.foundation.NamedElementBase;
import org.jooq.lambda.Seq;

import java.util.List;

import static java.lang.String.format;
import static org.jooq.lambda.Seq.seq;

public class Keyword extends NamedElementBase
{
    private final List<String> parameters;
    private final TempExpression expression;

    public Keyword(final String name, final Seq<String> parameters, final TempExpression expression)
    {
        super(name);

        this.parameters = parameters.toList();
        this.expression = expression;
    }

    public Seq<String> getParameters()
    {
        return seq(parameters);
    }

    public TempExpression getExpression()
    {
        return expression;
    }

    public TempExpression getLambdaExpression()
    {
        return new Lambda(seq(parameters), expression);
    }

    @Override
    public String toString()
    {
        return parameters.isEmpty() ?
            format("%s: %s }", getName(), expression) :
            format("{ %s: %s -> %s }", getName(), seq(parameters).toString(", "), expression);
    }
}
