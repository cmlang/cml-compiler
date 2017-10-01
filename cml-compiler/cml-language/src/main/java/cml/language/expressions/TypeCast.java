package cml.language.expressions;

import cml.language.generated.Type;
import cml.language.types.NamedType;
import cml.language.types.TempType;

import static cml.language.functions.TypeFunctions.isCastAllowed;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

public class TypeCast extends ExpressionBase
{
    public static final String ASB = "asb";
    public static final String ASQ = "asq";

    private final TempExpression expr;
    private final String operator;
    private final TempType castType;

    public TypeCast(TempExpression expr, final String operator, final TempType castType)
    {
        super(singletonList(expr));
        this.expr = expr;
        this.operator = operator;
        this.castType = castType;
    }

    public TempExpression getExpr()
    {
        return expr;
    }

    public String getOperator()
    {
        return operator;
    }

    public TempType getCastType()
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

        if (isCastAllowed(operator, exprType, castType))
        {
            return castType;
        }
        else
        {
            return NamedType.createUndefined(
                format(
                    "%s:\n- left operand is '%s: %s'\n- right operand is '%s'",
                    diagnosticMessage(operator, (TempType) exprType, castType), expr, exprType, castType));
        }
    }

    private static String diagnosticMessage(String operator, TempType exprType, TempType castType)
    {
        if (operator.equals(ASQ) && castType.isRequired())
        {
            return "Cannot use 'as?' to cast to required-cardinality type";
        }
        else if (operator.equals(ASB) && !exprType.isRequired() && castType.isRequired())
        {
            return "Cannot cast from optional or sequence type to required-cardinality type";
        }
        else if (exprType.isSequence() && castType.isOptional())
        {
            return "Cannot cast from sequence type to optional-cardinality type";
        }
        else
        {
            return format("Incompatible operand(s) for operator '%s'",
                          operator.replace('q', '?').replace('b', '!'));
        }
    }

}
