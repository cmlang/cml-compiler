package cml.language.expressions;

import cml.language.types.NamedType;
import cml.language.types.Type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

public class Infix extends ExpressionBase
{
    private final Collection<String> MATH_OPERATORS = unmodifiableCollection(asList(
        "+", "-", "*", "/", "%", "^" // arithmetic operators
    ));

    private final Collection<String> LOGIC_OPERATORS = unmodifiableCollection(asList(
        "==", "!=", ">", ">=", "<", "<=", // relational operators
        "and", "or", "xor", "implies" // boolean operators
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
            put("!=", "ineq");
            put(">", "gt");
            put(">=", "gte");
            put("<", "lt");
            put("<=", "lte");

            // Boolean Operators:
            put("and", "and");
            put("or", "or");
            put("xor", "xor");
            put("implies", "implies");
        }};

    private final String operator;
    private final Expression left;
    private final Expression right;

    public Infix(String operator, Expression left, Expression right)
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

    public Expression getLeft()
    {
        return left;
    }

    public Expression getRight()
    {
        return right;
    }

    @Override
    public String getKind()
    {
        return "infix";
    }

    @Override
    public Type getType()
    {
        final Type leftType = left.getType();
        final Type rightType = right.getType();

        assert leftType != null: "Left expression must have a type in order to be able to compute type of infix expression: " + left.getKind();
        assert rightType != null: "Right expression must have a type in order to be able to compute type of infix expression: " + right.getKind();

        if (LOGIC_OPERATORS.contains(operator))
        {
            return NamedType.BOOLEAN;
        }
        else if (MATH_OPERATORS.contains(operator) && leftType.isNumeric() && rightType.isNumeric())
        {
            return leftType.isNumericWiderThan(rightType) ? leftType : rightType;
        }
        else if (MATH_OPERATORS.contains(operator) && leftType.isBinaryFloatingPoint() && rightType.isBinaryFloatingPoint())
        {
            return leftType.isBinaryFloatingPointWiderThan(rightType) ? leftType : rightType;
        }
        else if (leftType.isAssignableFrom(rightType))
        {
            return leftType;
        }
        else if (rightType.isAssignableFrom(leftType))
        {
            return rightType;
        }
        else
        {
            return NamedType.createUndefined(format("Unsupported operands for operator '%s'.", getOperator()));
        }
    }

    @Override
    public String toString()
    {
        return format("%s %s %s", getLeft(), getOperator(), getRight());
    }
}
