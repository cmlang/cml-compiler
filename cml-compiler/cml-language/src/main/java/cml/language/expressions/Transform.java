package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Transform extends ModelElement
{
    String getOperation();
    Optional<Expression> getExpr();
    Optional<String> getSuffix();
    Optional<String> getVariable();
    Optional<Expression> getInit();

    static Transform create(String operation)
    {
        return new TransformImpl(operation, null, null, null, null);
    }

    static Transform create(String operation, Expression expr)
    {
        return new TransformImpl(operation, expr, null, null, null);
    }

    static Transform create(String operation, Expression expr, String suffix)
    {
        return new TransformImpl(operation, expr, suffix, null, null);
    }

    static Transform create(String operation, Expression expr, String suffix, String variable, Expression init)
    {
        return new TransformImpl(operation, expr, suffix, variable, init);
    }
}

class TransformImpl implements Transform
{
    private final ModelElement modelElement;

    private final String operation;
    private final @Nullable Expression expr;
    private final @Nullable String suffix;
    private final @Nullable String variable;
    private final @Nullable Expression init;

    TransformImpl(
        String operation,
        @Nullable Expression expr,
        @Nullable String suffix,
        @Nullable String variable,
        @Nullable Expression init)
    {
        this.modelElement = ModelElement.create(this);
        this.operation = operation;
        this.expr = expr;
        this.suffix = suffix;
        this.variable = variable;
        this.init = init;
    }

    @Override
    public String getOperation()
    {
        return operation;
    }

    @Override
    public Optional<Expression> getExpr()
    {
        return Optional.ofNullable(expr);
    }

    @Override
    public Optional<String> getSuffix()
    {
        return Optional.ofNullable(suffix);
    }

    @Override
    public Optional<String> getVariable()
    {
        return Optional.ofNullable(variable);
    }

    @Override
    public Optional<Expression> getInit()
    {
        return Optional.ofNullable(init);
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }
}
