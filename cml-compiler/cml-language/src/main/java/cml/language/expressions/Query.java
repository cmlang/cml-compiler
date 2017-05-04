package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Query extends Expression
{
    Expression getBase();
    Transform getTransform();

    static Query create(Expression expr, Transform transform)
    {
        return new QueryImpl(expr, transform, null);
    }

    static Query create(Expression expr, Transform transform, @Nullable Type type)
    {
        return new QueryImpl(expr, transform, type);
    }
}

class QueryImpl implements Query
{
    private final ModelElement modelElement;
    private final Expression expression;

    private final Expression base;
    private final Transform transform;

    QueryImpl(Expression base, Transform transform, @Nullable Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.expression = Expression.create(modelElement, "query", type);
        this.base = base;
        this.transform = transform;
    }

    @Override
    public Expression getBase()
    {
        return base;
    }

    @Override
    public Transform getTransform()
    {
        return transform;
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
