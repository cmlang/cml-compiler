package cml.language.loader;

import cml.language.expressions.Expression;
import cml.language.expressions.Invocation;

public class LambdaScopeLinker implements ModelVisitor
{
    @Override
    public void visit(final Expression expression)
    {
        if (expression instanceof Invocation)
        {
            final Invocation invocation = (Invocation) expression;

            if (invocation.getFunction().isPresent())
            {
                invocation.getTypedLambdaArguments().forEach(
                    (functionType, lambda) ->
                    {
                        if (lambda.getFunctionType().isPresent() && !lambda.isExpressionInSomeScope())
                        {
                            invocation.getExpressionScopeFor(lambda).ifPresent(lambda::addExpressionToScope);
                        }
                    });
            }
        }
    }
}
