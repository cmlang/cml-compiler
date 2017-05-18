package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;

import java.util.*;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

public interface Infix extends Expression
{
    String getOperator();
    Optional<String> getOperation();
    Expression getLeft();
    Expression getRight();

    static Infix create(String operator, Expression left, Expression right)
    {
        return new InfixImpl(operator, left, right);
    }
}

class InfixImpl implements Infix
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

    private final ModelElement modelElement;
    private final Scope scope;

    private final String operator;
    private final Expression left;
    private final Expression right;

    InfixImpl(String operator, Expression left, Expression right)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);
        
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public String getOperator()
    {
        return operator;
    }

    @Override
    public Optional<String> getOperation()
    {
        final String operation = OPERATIONS.get(getOperator());

        return Optional.ofNullable(operation);
    }

    @Override
    public Expression getLeft()
    {
        return left;
    }

    @Override
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
            return Type.BOOLEAN;
        }
        if (MATH_OPERATORS.contains(operator) && leftType.isOrdinal() && rightType.isOrdinal())
        {
            return leftType.isGreaterThan(rightType) ? leftType : rightType;
        }
        else if (leftType.equals(rightType))
        {
            return leftType;
        }
        else
        {
            System.out.println("Undefined type for: " + toString());
            System.out.println(">>> Left Type: " + leftType);
            System.out.println(">>> Right Type: " + rightType);

            return Type.UNDEFINED;
        }
    }

    @Override
    public void addMember(ModelElement member)
    {
        scope.addMember(member);
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }

    @Override
    public String toString()
    {
        return format("%s %s %s", getLeft(), getOperator(), getRight());
    }
}
