package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.generated.Location;
import cml.language.types.NamedType;
import cml.language.types.Type;
import org.jetbrains.annotations.Nullable;

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
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public void setLocation(@Nullable Location location)
    {
        modelElement.setLocation(location);
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
        final Type thenType = then.getType();
        final Type elseType = else_.getType();

        return thenType.equals(elseType) ? thenType : NamedType.create(thenType + "|" + elseType);
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
}