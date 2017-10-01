package cml.language.expressions;

import cml.language.generated.Expression;
import cml.language.generated.Type;
import cml.language.types.NamedType;

import static cml.language.functions.TypeFunctions.isAssignableFrom;
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
        else
        {
            return NamedType.create(thenType + "|" + elseType);
        }
    }
}