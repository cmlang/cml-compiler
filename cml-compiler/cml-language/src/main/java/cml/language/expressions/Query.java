package cml.language.expressions;

import cml.language.foundation.ModelElementBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;

public class Query extends ModelElementBase
{
    private final @Nullable String name;
    private final List<Keyword> keywords;

    public Query(final @NotNull String name)
    {
        this.name = name;
        this.keywords = emptyList();
    }

    public Query(final Stream<Keyword> keywords)
    {
        this.name = null;
        this.keywords = keywords.collect(toList());
    }

    public List<Keyword> getKeywords()
    {
        return unmodifiableList(keywords);
    }

    public String getInvocationName()
    {
        final Optional<Keyword> first = seq(keywords).findFirst();

        assert name != null || first.isPresent();

        return first.map(Keyword::getName).orElse(name);
    }

    public Optional<Expression> getExpression()
    {
        final Optional<Keyword> first = seq(keywords).findFirst();

        return first.map(Keyword::getExpression);
    }

    public List<Keyword> getExtraKeywords()
    {
        return seq(keywords).skip(1).toList();
    }
}

