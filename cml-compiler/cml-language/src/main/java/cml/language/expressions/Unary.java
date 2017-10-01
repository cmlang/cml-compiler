package cml.language.expressions;

import cml.language.generated.Type;
import cml.language.types.NamedType;
import cml.language.types.TempType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableCollection;

public class Unary extends ExpressionBase
{
    private final Collection<String> LOGIC_OPERATORS = unmodifiableCollection(singletonList(
        "not"
    ));

    private final Collection<String> NUMERIC_OPERATORS = unmodifiableCollection(asList(
        "+", "-"
    ));

    private static Map<String, String> OPERATIONS =
        new HashMap<String, String>()
        {{
            put("+", "add");
            put("-", "sub");
            put("not", "not");
        }};

    private final String operator;
    private final TempExpression subExpr;

    public Unary(String operator, TempExpression subExpr)
    {
        super(singletonList(subExpr));

        this.operator = operator;
        this.subExpr = subExpr;
    }

    public String getOperator()
    {
        return operator;
    }

    public Optional<String> getOperation()
    {
        final String operation = OPERATIONS.get(getOperator());

        return Optional.ofNullable(operation);
    }

    public TempExpression getSubExpr()
    {
        return subExpr;
    }

    @Override
    public String getKind()
    {
        return "unary";
    }

    @Override
    public Type getType()
    {
        final TempType subExprType = (TempType) subExpr.getType();

        if (subExprType.isUndefined())
        {
            return subExprType;
        }
        else if (LOGIC_OPERATORS.contains(operator) && subExprType.isBoolean())
        {
            return NamedType.BOOLEAN;
        }
        else if (NUMERIC_OPERATORS.contains(operator) && (subExprType.isNumeric() || subExprType.isBinaryFloatingPoint()))
        {
            return subExprType;
        }
        else
        {
            return NamedType.createUndefined(
                format(
                    "Incompatible operand '%s' of type '%s' for operator '%s'.",
                    getSubExpr(), subExprType, getOperator()));
        }
    }
}
