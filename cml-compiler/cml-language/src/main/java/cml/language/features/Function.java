package cml.language.features;

import cml.language.types.Type;
import cml.language.types.TypedElementBase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public int getParamIndexOfMatchingType()
    {
        assert getType().isParameter(): "Must be called only when macro's resulting type is a parameter.";

        int index = 0;
        for (FunctionParameter parameter: getParameters())
        {
            if (parameter.getType().equals(getType()))
            {
                break;
            }
            index++;
        }

        assert index < getParameters().size(): "Expected to find a macro parameter with a type matching the resulting type.";

        return index;
    }
}
