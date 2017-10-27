package cml.language.loader;

import cml.language.expressions.Invocation;
import cml.language.foundation.TempModel;
import cml.language.generated.Expression;

import static cml.language.functions.ModelFunctions.conceptOf;
import static cml.language.functions.ModelFunctions.functionOf;
import static cml.language.functions.ModelFunctions.templateOf;
import static org.jooq.lambda.Seq.seq;

public class FunctionLinker implements ModelVisitor
{
    private TempModel model;

    @Override
    public void visit(final TempModel model)
    {
        this.model = model;
    }

    @Override
    public void visit(final Expression expression)
    {
        if (expression instanceof Invocation)
        {
            final Invocation invocation = (Invocation) expression;

            if (!invocation.getFunction().isPresent() && !invocation.getConcept().isPresent())
            {
                functionOf(model, invocation.getName()).ifPresent(f -> invocation.setFunction(f));

                if (!invocation.getFunction().isPresent())
                {
                    templateOf(model, invocation.getName()).ifPresent(t -> invocation.setFunction(t.getFunction()));
                }

                if (invocation.getFunction().isPresent())
                {
                    seq(invocation.getTypedLambdaArguments()).filter(t -> !t.v2.getFunctionType().isPresent())
                                                             .forEach(t -> t.v2.setFunctionType(t.v1));
                }
            }

            if (!invocation.getFunction().isPresent() && !invocation.getConcept().isPresent())
            {
                conceptOf(model, invocation.getName()).ifPresent(c -> invocation.setConcept(c));
            }
        }
    }
}
