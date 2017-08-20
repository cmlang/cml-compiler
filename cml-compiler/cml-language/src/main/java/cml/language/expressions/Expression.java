package cml.language.expressions;

import cml.language.foundation.*;
import cml.language.types.Type;
import org.jooq.lambda.Seq;

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.jooq.lambda.Seq.seq;

public interface Expression extends ModelElement, Scope
{
    String getKind();
    Type getType();

    default Type getMatchingResultType()
    {
        return getType();
    }

    default Seq<Expression> getSubExpressions()
    {
        return seq(getMembers()).filter(m -> m instanceof Expression)
                                .map(m -> (Expression)m);
    }

    default boolean evaluateInvariants()
    {
        return true;
    }

    static InvariantValidator<Expression> invariantValidator()
    {
        return () -> singletonList(new ExpressionInvariant());
    }

    default Diagnostic createDiagnostic()
    {
        throw new UnsupportedOperationException("createDiagnostic");
    }
}

class ExpressionInvariant implements Invariant<Expression>
{
    @Override
    public boolean isCritical()
    {
        return true;
    }

    @Override
    public boolean evaluate(final Expression self)
    {
        for (final Expression expr: self.getSubExpressions())
        {
            final boolean pass = expr.evaluateInvariants();

            if (!pass) return false;
        }

        return self.evaluateInvariants();
    }

    @Override
    public Diagnostic createDiagnostic(final Expression self)
    {
        for (final Expression expr: self.getSubExpressions())
        {
            final boolean pass = expr.evaluateInvariants();

            if (!pass) return expr.createDiagnostic();
        }

        return self.createDiagnostic();
    }
}

