package cml.language.transforms;

import cml.language.expressions.*;

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

        final Query query = first.get();
        final String name = query.getInvocationName();
        final LinkedHashMap<String, Expression> arguments = new LinkedHashMap<>();

        final List<Query> rest = seq(queries).skip(1).toList();

        if (rest.isEmpty())
        {
            if (comprehension.getPath().isPresent())
            {
                arguments.put("seq", comprehension.getPath().get());
            }
            else if (comprehension.getEnumerators().size() == 1)
            {
                seq(comprehension.getEnumerators()).findFirst()
                                                   .ifPresent(e -> arguments.put("seq", e.getPath()));
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

        query.getExpression().ifPresent(expr -> arguments.put(
            "expr", new Lambda(comprehension.getEnumeratorVariablesForQuery(query), expr)));

        query.getExtraKeywords().forEach(k -> arguments.put(k.getName(), k.getExpression()));

        return Invocation.create(name, arguments);
    }
}
