package cml.language.expressions;

import cml.language.types.NamedType;
import cml.language.types.TempType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static cml.language.functions.TypeFunctions.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

public class Infix extends ExpressionBase
{
    private static final String REFERENTIAL_EQUALITY = "===";
    private static final String REFERENTIAL_INEQUALITY = "!==";

    private static final Collection<String> ARITHMETIC_OPERATORS = unmodifiableCollection(asList(
        "+", "-", "*", "/", "%", "^"
    ));

    private static final Collection<String> RELATIONAL_OPERATORS = unmodifiableCollection(asList(
        "==", "!=", ">", ">=", "<", "<="
    ));

    private static final Collection<String> BOOLEAN_OPERATORS = unmodifiableCollection(asList(
        "and", "or", "xor", "implies"
    ));

    private static final Collection<String> REFERENTIAL_OPERATORS = unmodifiableCollection(asList(
        REFERENTIAL_EQUALITY, REFERENTIAL_INEQUALITY
    ));

    private static Map<String, String> OPERATIONS =
        new HashMap<String, String>()
        {{
            // Arithmetic Operators:
            put("+", "add");
            put("-", "sub");
            put("*", "mul");
            put("/", "div");
            put("%", "mod");
            put("^", "exp");

            // Relational Operators:
            put("==", "eq");
            put("!=", "not_eq");
            put(">", "gt");
            put(">=", "gte");
            put("<", "lt");
            put("<=", "lte");

            // Boolean Operators:
            put("and", "and");
            put("or", "or");
            put("xor", "xor");
            put("implies", "implies");

            // Referential Operators:
            put(REFERENTIAL_EQUALITY, "ref_eq");
            put(REFERENTIAL_INEQUALITY, "not_ref_eq");
        }};

    private final String operator;
    private final TempExpression left;
    private final TempExpression right;

    public Infix(String operator, TempExpression left, TempExpression right)
    {
        super(asList(left, right));

        this.operator = operator;
        this.left = left;
        this.right = right;
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

    public TempExpression getLeft()
    {
        return left;
    }

    public TempExpression getRight()
    {
        return right;
    }

    @Override
    public String getKind()
    {
        return "infix";
    }

    @Override
    public TempType getType()
    {
        final TempType leftType = left.getType();
        final TempType rightType = right.getType();

        assert leftType != null: "Left expression must have a type in order to be able to compute type of infix expression: " + left.getKind();
        assert rightType != null: "Right expression must have a type in order to be able to compute type of infix expression: " + right.getKind();

        if (leftType.isUndefined())
        {
            return leftType;
        }
        else if (rightType.isUndefined())
        {
            return rightType;
        }
        else if (BOOLEAN_OPERATORS.contains(operator) && leftType.isBoolean() && rightType.isBoolean())
        {
            return NamedType.BOOLEAN;
        }
        else if (RELATIONAL_OPERATORS.contains(operator) && leftType.isRelational() && rightType.isRelational())
        {
            return NamedType.BOOLEAN;
        }
        else if (REFERENTIAL_OPERATORS.contains(operator) && leftType.isReferential() && rightType.isReferential())
        {
            return NamedType.BOOLEAN;
        }
        else if (ARITHMETIC_OPERATORS.contains(operator) && leftType.isNumeric() && rightType.isNumeric())
        {
            return isNumericWiderThan(leftType, rightType) ? leftType : rightType;
        }
        else if (ARITHMETIC_OPERATORS.contains(operator) && leftType.isBinaryFloatingPoint() && rightType.isBinaryFloatingPoint())
        {
            return isBinaryFloatingPointWiderThan(leftType, rightType) ? leftType : rightType;
        }
        else
        {
            return NamedType.createUndefined(
                format(
                    "Incompatible operand(s) for operator '%s':\n- left operand is '%s: %s'\n- right operand is '%s: %s'",
                    getOperator(), getLeft(), leftType, getRight(), rightType));
        }
    }

    @Override
    public String toString()
    {
        return format("%s %s %s", getLeft(), getOperator(), getRight());
    }
}
