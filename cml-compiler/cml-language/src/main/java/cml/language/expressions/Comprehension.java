package cml.language.expressions;

import cml.language.types.NamedType;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;

public class Comprehension extends ExpressionBase
{
    private final @Nullable Path path;
    private final List<Enumerator> enumerators;
    private final List<Query> queries;

    public Comprehension(
        @SuppressWarnings("NullableProblems") final Path path,
        final Stream<Query> queries)
    {
        this.path = path;
        this.enumerators = emptyList();
        this.queries = queries.collect(toList());
    }

    public Comprehension(
        final Stream<Enumerator> enumerators,
        final Stream<Query> queries)
    {
        this.path = null;
        this.enumerators = enumerators.collect(toList());
        this.queries = queries.collect(toList());
    }

    public Optional<Path> getPath()
    {
        return Optional.ofNullable(path);
    }

    public List<Enumerator> getEnumerators()
    {
        return unmodifiableList(enumerators);
    }

    public List<Query> getQueries()
    {
        return unmodifiableList(queries);
    }

    public List<Expression> getExpressions()
    {
        return seq(enumerators).map(Enumerator::getPath)
                               .collect(toList());
    }

    public Seq<String> getEnumeratorVariablesForQuery(final Query query)
    {
        return seq(enumerators).map(Enumerator::getVariable)
                               .concat(query.getExpressionParams());
    }

    @Override
    public String getKind()
    {
        return "comprehension";
    }

    @Override
    public NamedType getType()
    {
        return null;
    }
}
