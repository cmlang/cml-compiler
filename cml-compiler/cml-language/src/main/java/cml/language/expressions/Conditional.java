package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;

import java.util.List;
import java.util.Optional;

public interface Conditional extends Expression
{
    Expression getCond();
    Expression getThen();
    Expression getElse_();

    static Conditional create(Expression cond, Expression then, Expression else_)
    {
        return new ConditionalImpl(cond, then, else_);
    }
}

class ConditionalImpl implements Conditional
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final Expression cond;
    private final Expression then;
    private final Expression else_;

    ConditionalImpl(Expression cond, Expression then, Expression else_)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

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
        return "conditional";
    }

    @Override
    public Type getType()
    {
        final String thenType = then.getType().getName();
        final String elseType = else_.getType().getName();
        final String typeName = thenType.equals(elseType) ? thenType : thenType + "|" + elseType;

        return Type.create(typeName);
    }

    @Override
    public void addElement(ModelElement element)
    {
        scope.addElement(element);
    }

    @Override
    public List<ModelElement> getElements()
    {
        return scope.getElements();
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }
}