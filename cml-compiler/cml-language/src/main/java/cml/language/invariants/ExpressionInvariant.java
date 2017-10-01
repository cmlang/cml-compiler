package cml.language.invariants;

import cml.language.expressions.Invocation;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Expression;

public class ExpressionInvariant implements Invariant<Expression>
{
    private final InvocationInvariant invocationInvariant = new InvocationInvariant();

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
            final boolean pass = evaluateInvariantsOf(expr);

            if (!pass) return false;
        }

        return evaluateInvariantsOf(self);
    }

    @Override
    public Diagnostic createDiagnostic(final Expression self)
    {
        for (final Expression expr: self.getSubExpressions())
        {
            final boolean pass = evaluateInvariantsOf(expr);

            if (!pass) return createDiagnosticOf(expr);
        }

        return createDiagnosticOf(self);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean evaluateInvariantsOf(final Expression expr)
    {
        if (expr instanceof Invocation) return invocationInvariant.evaluate((Invocation) expr);
        else return true;
    }

    private Diagnostic createDiagnosticOf(final Expression expr)
    {
        if (expr instanceof Invocation) return invocationInvariant.createDiagnostic((Invocation) expr);
        else throw new IllegalArgumentException("Unexpected diagnostic for expression: " + expr);
    }
}
