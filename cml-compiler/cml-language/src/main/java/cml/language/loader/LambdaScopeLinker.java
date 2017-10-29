package cml.language.loader;

import cml.language.expressions.Invocation;
import cml.language.generated.Expression;

public class LambdaScopeLinker implements ModelVisitor
{
    @Override
    public void visit(final Expression expression)
    {
        if (expression instanceof Invocation)
        {
            final Invocation invocation = (Invocation) expression;

            // First, lets arguments link scope of its own lambdas.
            // E.g.: select(recurse(self, { children }), { ranking > 10 })

            invocation.getArguments().forEach(this::visit);

            if (invocation.getFunction().isPresent())
            {
                invocation.getTypedLambdaArguments().forEach(
                    (functionType, lambda) ->
                    {
                        if (lambda.getFunctionType().isPresent() && !lambda.isInnerExpressionInSomeScope())
                        {
                            invocation.createScopeFor(lambda);
                        }
                    });

                invocation.getUntypedParameterlessLambdaArguments().forEach(
                    (functionType, lambda) ->
                    {
                        if (!lambda.isInnerExpressionInSomeScope())
                        {
                            invocation.createScopeFor(lambda);
                        }
                    });
            }
        }
    }
}
