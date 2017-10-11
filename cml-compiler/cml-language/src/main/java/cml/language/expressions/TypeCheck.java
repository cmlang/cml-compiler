package cml.language.expressions;

import cml.language.generated.Expression;
import cml.language.generated.Type;
import cml.language.types.TempNamedType;

import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

public class TypeCheck extends ExpressionBase
{
    private final Expression expr;
    private final String operator;
    private final Type checkedType;

    public TypeCheck(Expression expr, final String operator, final Type checkedType)
    {
        super(singletonList(expr));
        this.expr = expr;
        this.operator = operator;
        this.checkedType = checkedType;
    }

    public Expression getExpr()
    {
        return expr;
    }

    public String getOperator()
    {
        return operator;
    }

    public Type getCheckedType()
    {
        return checkedType;
    }

    @Override
    public String getKind()
    {
        return "type_check";
    }

    @Override
    public Type getType()
    {
        final Type exprType = (Type) expr.getType();

        if (exprType.isReferential() && checkedType.isReferential() && isAssignableFrom(exprType, checkedType))
        {
            return TempNamedType.BOOLEAN;
        }
        else
        {
            return TempNamedType.createUndefined(
                format(
                    "Incompatible operand(s) for operator '%s':\n- left operand is '%s: %s'\n- right operand is '%s'",
                    getOperator(), expr, exprType, checkedType));
        }
    }
}
