package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Path extends Expression
{
    List<String> getNames();

    static Path create(List<String> names)
    {
        return new PathImpl(names, null);
    }

    static Path create(List<String> names, @Nullable Type type)
    {
        return new PathImpl(names, type);
    }

}

class PathImpl implements Path
{
    private final ModelElement modelElement;
    private final Expression expression;

    private final List<String> names;

    PathImpl(List<String> names, @Nullable Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.expression = Expression.create(modelElement, "path", type);
        this.names = new ArrayList<>(names);
    }

    @Override
    public List<String> getNames()
    {
        return new ArrayList<>(names);
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
