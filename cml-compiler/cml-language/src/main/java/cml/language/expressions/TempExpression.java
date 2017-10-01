package cml.language.expressions;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.foundation.InvariantValidator;
import cml.language.generated.Expression;
import cml.language.generated.ModelElement;
import cml.language.generated.Scope;
import cml.language.types.TempType;

import java.util.List;

import static java.util.Collections.singletonList;
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

    default boolean evaluateInvariants()
    {
        return true;
    }

    static InvariantValidator<TempExpression> invariantValidator()
    {
        return () -> singletonList(new ExpressionInvariant());
    }

    default Diagnostic createDiagnostic()
    {
        throw new UnsupportedOperationException("createDiagnostic");
    }
}

class ExpressionInvariant implements Invariant<TempExpression>
{
    @Override
    public boolean isCritical()
    {
        return true;
    }

    @Override
    public boolean evaluate(final TempExpression self)
    {
        for (final TempExpression expr: self.getSubExpressions())
        {
            final boolean pass = expr.evaluateInvariants();

            if (!pass) return false;
        }

        return self.evaluateInvariants();
    }

    @Override
    public Diagnostic createDiagnostic(final TempExpression self)
    {
        for (final TempExpression expr: self.getSubExpressions())
        {
            final boolean pass = expr.evaluateInvariants();

            if (!pass) return expr.createDiagnostic();
        }

        return self.createDiagnostic();
    }
}

