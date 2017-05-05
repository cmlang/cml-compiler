package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface Join extends Expression
{
    List<String> getVariables();
    List<Path> getPaths();
    boolean isComplete();
    Path getFirstPath();

    static Join create(List<String> variables, List<Path> paths)
    {
        return new JoinImpl(variables, paths, null);
    }

    static Join create(List<String> variables, List<Path> paths, @Nullable Type type)
    {
        return new JoinImpl(variables, paths, type);
    }
}

class JoinImpl implements Join
{
    private final ModelElement modelElement;
    private final Expression expression;

    private final List<String> variables;
    private final List<Path> paths;

    JoinImpl(List<String> variables, List<Path> paths, @Nullable Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.expression = Expression.create(modelElement, "join", type);

        this.variables = new ArrayList<>(variables);
        this.paths = new ArrayList<>(paths);
    }

    @Override
    public List<String> getVariables()
    {
        return new ArrayList<>(variables);
    }

    @Override
    public List<Path> getPaths()
    {
        return new ArrayList<>(paths);
    }

    @Override
    public boolean isComplete()
    {
        return getPaths().size() > 1;
    }

    @Override
    public Path getFirstPath()
    {
        assert getPaths().size() > 0;

        return getPaths().get(0);
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
