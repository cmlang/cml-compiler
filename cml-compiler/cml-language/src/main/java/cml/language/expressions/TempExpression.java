package cml.language.expressions;

import cml.language.generated.Expression;
import cml.language.types.TempType;

import java.util.List;

import static org.jooq.lambda.Seq.seq;

public interface TempExpression extends Expression
{
    String getKind();
    TempType getType();

    default TempType getMatchingResultType()
    {
        return getType();
    }

    default List<TempExpression> getSubExpressions()
    {
        return seq(getMembers()).filter(m -> m instanceof TempExpression)
                                .map(m -> (TempExpression)m)
                                .toList();
    }
}

