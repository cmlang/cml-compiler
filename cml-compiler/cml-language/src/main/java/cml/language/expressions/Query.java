package cml.language.expressions;

import cml.language.foundation.ModelElementBase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;

public class Query extends ModelElementBase
{
    private final List<Keyword> keywords;

    public Query(final Stream<Keyword> keywords)
    {
        this.keywords = keywords.collect(toList());
    }

    public List<Keyword> getKeywords()
    {
        return unmodifiableList(keywords);
    }

    public String getInvocationName()
    {
        final Optional<Keyword> first = seq(keywords).findFirst();
        assert first.isPresent();

        return first.get().getName();
    }

    public Expression getExpression()
    {
        final Optional<Keyword> first = seq(keywords).findFirst();
        assert first.isPresent();

        return first.get().getExpression();
    }
}

