package cml.language.expressions;

import cml.language.generated.Expression;
import cml.language.generated.Type;
import cml.language.types.TempNamedType;

import java.util.Optional;

import static cml.language.functions.TypeFunctions.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class Conditional extends ExpressionBase
{
    private final Expression cond;
    private final Expression then;
    private final Expression else_;

    public Conditional(Expression cond, Expression then, Expression else_)
    {
        super(asList(cond, then, else_));

        this.cond = cond;
        this.then = then;
        this.else_ = else_;
    }

    public Expression getCond()
    {
        return cond;
    }

    public Expression getThen()
    {
        return then;
    }

    public Expression getElse_()
    {
        return else_;
    }

    @Override
    public String getKind()
    {
        return "conditional";
    }

    @Override
    public Type getType()
    {
        final Type thenType = then.getType();
        final Type elseType = else_.getType();

        if (isAssignableFrom(thenType, elseType))
        {
            return thenType;
        }
        else if (isAssignableFrom(elseType, thenType))
        {
            return elseType;
        }
        else if (thenType.getConcept().isPresent() && elseType.getConcept().isPresent())
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

        return TempNamedType.create(thenType.getDiagnosticId() + "|" + elseType.getDiagnosticId());
    }

    @Override
    public String getDiagnosticId()
    {
        return format("if %s then %s else %s", cond.getDiagnosticId(), then.getDiagnosticId(), else_.getDiagnosticId());
    }
}