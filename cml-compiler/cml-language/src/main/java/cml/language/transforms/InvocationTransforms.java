package cml.language.transforms;

import cml.language.expressions.Comprehension;
import cml.language.expressions.Expression;
import cml.language.expressions.Invocation;
import cml.language.expressions.Query;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.jooq.lambda.Seq.seq;

public class InvocationTransforms
{
    public static Invocation invocationOf(final Comprehension comprehension)
    {
        final List<Query> reversedQueries = seq(comprehension.getQueries()).reverse().toList();

        return invocationOf(comprehension, reversedQueries);
    }

    private static Invocation invocationOf(final Comprehension comprehension, final List<Query> queries)
    {
        final Optional<Query> first = seq(queries).findFirst();
        assert first.isPresent();

        final String name = first.get().getInvocationName();
        final LinkedHashMap<String, Expression> arguments = new LinkedHashMap<>();

        final List<Query> rest = seq(queries).skip(1).toList();

        if (rest.isEmpty())
        {
            if (comprehension.getPath().isPresent())
            {
                arguments.put("seq", comprehension.getPath().get());
            }
            else
            {
                return Invocation.create("cross_join", comprehension.getExpressions());
            }
        }
        else
        {
            arguments.put("seq", invocationOf(comprehension, rest));
        }

        arguments.put("expr", first.get().getExpression());

        return Invocation.create(name, arguments);
    }
}
