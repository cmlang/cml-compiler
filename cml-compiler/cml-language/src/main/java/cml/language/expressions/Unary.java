package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface Unary extends Expression
{
    String getOperator();
    Optional<String> getOperation();
    Expression getExpr();

    static Unary create(String operator, Expression expr)
    {
        return new UnaryImpl(operator, expr, null);
    }

    static Unary create(String operator, Expression expr, @Nullable Type type)
    {
        return new UnaryImpl(operator, expr, type);
    }
}

class UnaryImpl implements Unary
{
    private static Map<String, String> OPERATIONS =
        new HashMap<String, String>()
        {{
            put("+", "add");
            put("-", "sub");
            put("not", "not");
        }};

    private final ModelElement modelElement;
    private final Expression expression;
    private final String operator;
    private final Expression expr;

    UnaryImpl(String operator, Expression expr, @Nullable Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.expression = Expression.create(modelElement, "unary", type);
        this.operator = operator;
        this.expr = expr;
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
    public Expression getExpr()
    {
        return expr;
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
