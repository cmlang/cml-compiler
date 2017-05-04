package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface Infix extends Expression
{
    String getOperator();
    Optional<String> getOperation();
    Expression getLeft();
    Expression getRight();

    static Infix create(String operator, Expression left, Expression right)
    {
        return new InfixImpl(operator, left, right, null);
    }

    static Infix create(String operator, Expression left, Expression right, @Nullable Type type)
    {
        return new InfixImpl(operator, left, right, type);
    }
}

class InfixImpl implements Infix
{
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
    private final Expression expression;

    private final String operator;
    private final Expression left;
    private final Expression right;

    InfixImpl(String operator, Expression left, Expression right, @Nullable Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.expression = Expression.create(modelElement, "infix", type);
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
        return expression.getKind();
    }

    @Override
    public Optional<Type> getType()
    {
        return expression.getType();
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }
}
