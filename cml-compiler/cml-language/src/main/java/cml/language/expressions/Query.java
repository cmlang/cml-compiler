package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Query extends Expression
{
    Expression getBase();
    List<Transform> getTransforms();

    static Query create(Expression expr, List<Transform> transforms)
    {
        return new QueryImpl(expr, transforms, null);
    }

    static Query create(Expression expr, List<Transform> transforms, @Nullable Type type)
    {
        return new QueryImpl(expr, transforms, type);
    }
}

class QueryImpl implements Query
{
    private final ModelElement modelElement;
    private final Expression expression;

    private final Expression base;
    private final List<Transform> transforms;

    QueryImpl(Expression base, List<Transform> transforms, @Nullable Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.expression = Expression.create(modelElement, "query", type);
        this.base = base;
        this.transforms = new ArrayList<>(transforms);
    }

    @Override
    public Expression getBase()
    {
        return base;
    }

    @Override
    public List<Transform> getTransforms()
    {
        return new ArrayList<>(transforms);
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
