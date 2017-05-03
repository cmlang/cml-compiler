package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Conditional extends Expression
{
    Expression getCond();
    Expression getThen();
    Expression getElse_();

    static Conditional create(Expression cond, Expression then, Expression else_)
    {
        return new ConditionalImpl(cond, then, else_, null);
    }

    static Conditional create(Expression cond, Expression then, Expression else_, @Nullable Type type)
    {
        return new ConditionalImpl(cond, then, else_, type);
    }
}

class ConditionalImpl implements Conditional
{
    private final ModelElement modelElement;
    private final Expression expression;

    private final Expression cond;
    private final Expression then;
    private final Expression else_;

    ConditionalImpl(Expression cond, Expression then, Expression else_, @Nullable Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.expression = Expression.create(modelElement, "conditional", type);

        this.cond = cond;
        this.then = then;
        this.else_ = else_;
    }

    @Override
    public Expression getCond()
    {
        return cond;
    }

    @Override
    public Expression getThen()
    {
        return then;
    }

    @Override
    public Expression getElse_()
    {
        return else_;
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