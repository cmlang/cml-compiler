package cml.language.expressions;

import cml.language.types.NamedType;
import cml.language.types.Type;

import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

public class TypeCast extends ExpressionBase
{
    private final Expression expr;
    private final String operator;
    private final Type castType;

    public TypeCast(Expression expr, final String operator, final Type castType)
    {
        super(singletonList(expr));
        this.expr = expr;
        this.operator = operator;
        this.castType = castType;
    }

    public Expression getExpr()
    {
        return expr;
    }

    public String getOperator()
    {
        return operator;
    }

    public Type getCastType()
    {
        return castType;
    }

    @Override
    public String getKind()
    {
        return "type_cast";
    }

    @Override
    public Type getType()
    {
        final Type exprType = expr.getType();

        if (exprType.isReferential() && castType.isReferential() && isAssignableFrom(exprType, castType))
        {
            return castType;
        }
        else
        {
            return NamedType.createUndefined(
                format(
                    "Incompatible operand(s) for operator '%s':\n- left operand is '%s: %s'\n- right operand is '%s'",
                    getOperator(), expr, exprType, castType));
        }
    }
}
