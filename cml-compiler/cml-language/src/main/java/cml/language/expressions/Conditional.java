package cml.language.expressions;

import cml.language.generated.Type;
import cml.language.types.NamedType;

import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static java.util.Arrays.asList;

public class Conditional extends ExpressionBase
{
    private final TempExpression cond;
    private final TempExpression then;
    private final TempExpression else_;

    public Conditional(TempExpression cond, TempExpression then, TempExpression else_)
    {
        super(asList(cond, then, else_));

        this.cond = cond;
        this.then = then;
        this.else_ = else_;
    }

    public TempExpression getCond()
    {
        return cond;
    }

    public TempExpression getThen()
    {
        return then;
    }

    public TempExpression getElse_()
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