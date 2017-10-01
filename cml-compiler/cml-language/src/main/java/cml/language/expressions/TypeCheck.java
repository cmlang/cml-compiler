package cml.language.expressions;

import cml.language.types.NamedType;
import cml.language.types.TempType;

import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

public class TypeCheck extends ExpressionBase
{
    private final TempExpression expr;
    private final String operator;
    private final TempType checkedType;

    public TypeCheck(TempExpression expr, final String operator, final TempType checkedType)
    {
        super(singletonList(expr));
        this.expr = expr;
        this.operator = operator;
        this.checkedType = checkedType;
    }

    public TempExpression getExpr()
    {
        return expr;
    }

    public String getOperator()
    {
        return operator;
    }

    public TempType getCheckedType()
    {
        return checkedType;
    }

    @Override
    public String getKind()
    {
        return "type_check";
    }

    @Override
    public TempType getType()
    {
        final TempType exprType = expr.getType();

        if (exprType.isReferential() && checkedType.isReferential() && isAssignableFrom(exprType, checkedType))
        {
            return NamedType.BOOLEAN;
        }
        else
        {
            return NamedType.createUndefined(
                format(
                    "Incompatible operand(s) for operator '%s':\n- left operand is '%s: %s'\n- right operand is '%s'",
                    getOperator(), expr, exprType, checkedType));
        }
    }
}
