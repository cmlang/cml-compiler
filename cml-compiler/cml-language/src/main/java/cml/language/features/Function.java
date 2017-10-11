package cml.language.features;

import cml.language.generated.Type;
import cml.language.types.TypedElementBase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.jooq.lambda.Seq.seq;

public class Function extends TypedElementBase
{
    private List<FunctionParameter> parameters;

    public Function(final String name, final Type type, final Stream<FunctionParameter> parameters)
    {
        super(name, type);

        this.parameters = seq(parameters).toList();
    }

    public List<FunctionParameter> getParameters()
    {
        return unmodifiableList(parameters);
    }

    public Optional<FunctionParameter> getParameter(String name)
    {
        return seq(parameters).filter(p -> p.getName().equals(name))
                              .findFirst();
    }

    @Override
    public String toString()
    {
        return format("function %s(%s) -> %s", getName(), seq(parameters).toString(", "), getType());
    }
}
