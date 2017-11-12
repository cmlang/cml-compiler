package cml.language.expressions;

import cml.language.generated.Concept;
import cml.language.generated.Conditional;
import cml.language.generated.Expression;
import cml.language.generated.Type;
import cml.language.types.TempNamedType;

import java.util.Optional;

import static cml.language.functions.ModelFunctions.conceptOf;
import static cml.language.functions.TypeFunctions.firstGeneralizationAssignableFrom;
import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static cml.language.generated.Conditional.extendConditional;
import static java.util.Arrays.asList;

public class TempConditional extends ExpressionBase implements Conditional
{
    private final Conditional conditional;

    public TempConditional(Expression cond, Expression then, Expression else_)
    {
        super(asList(cond, then, else_));

        this.conditional = extendConditional(this, expression, expression, expression, expression);
    }

    @Override
    public String getKind()
    {
        return conditional.getKind();
    }

    @Override
    public Expression getCondExpr()
    {
        return conditional.getCondExpr();
    }

    @Override
    public Expression getThenExpr()
    {
        return conditional.getThenExpr();
    }

    @Override
    public Expression getElseExpr()
    {
        return conditional.getElseExpr();
    }

    @Override
    public Type getType()
    {
        final Type thenType = getThenExpr().getType();
        final Type elseType = getElseExpr().getType();

        if (isAssignableFrom(thenType, elseType))
        {
            return thenType;
        }
        else if (isAssignableFrom(elseType, thenType))
        {
            return elseType;
        }
        else
        {
            final Optional<Concept> concept1 = getModel().flatMap(m -> conceptOf(m, thenType.getName()));
            final Optional<Concept> concept2 = getModel().flatMap(m -> conceptOf(m, elseType.getName()));

            if (concept1.isPresent() && concept2.isPresent())
            {
                final Optional<Type> thenTypeGeneralization = firstGeneralizationAssignableFrom(thenType, elseType);

                if (thenTypeGeneralization.isPresent())
                {
                    return thenTypeGeneralization.get();
                }
                else
                {
                    final Optional<Type> elseTypeGeneralization = firstGeneralizationAssignableFrom(thenType, elseType);

                    if (elseTypeGeneralization.isPresent())
                    {
                        return elseTypeGeneralization.get();
                    }
                }
            }
        }

        return TempNamedType.create(thenType.getDiagnosticId() + "|" + elseType.getDiagnosticId());
    }

    @Override
    public String getDiagnosticId()
    {
        return conditional.getDiagnosticId();
    }
}