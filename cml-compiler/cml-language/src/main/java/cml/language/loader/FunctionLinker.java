package cml.language.loader;

import cml.language.expressions.Expression;
import cml.language.expressions.Invocation;
import cml.language.foundation.Model;

import static cml.language.functions.ModelFunctions.templateOf;
import static org.jooq.lambda.Seq.seq;

public class FunctionLinker implements ModelVisitor
{
    private Model model;

    @Override
    public void visit(final Model model)
    {
        this.model = model;
    }

    @Override
    public void visit(final Expression expression)
    {
        if (expression instanceof Invocation)
        {
            final Invocation invocation = (Invocation) expression;

            if (!invocation.getFunction().isPresent())
            {
                templateOf(model, invocation.getName()).ifPresent(t -> invocation.setFunction(t.getFunction()));

                if (invocation.getFunction().isPresent())
                {
                    seq(invocation.getTypedLambdaArguments()).filter(t -> !t.v2.getFunctionType().isPresent())
                                                             .forEach(t -> t.v2.setFunctionType(t.v1));
                }
            }
        }
    }
}
