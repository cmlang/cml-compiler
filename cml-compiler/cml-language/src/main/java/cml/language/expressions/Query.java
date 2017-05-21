package cml.language.expressions;

import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface Query extends Expression
{
    Expression getBase();
    Transform getTransform();

    default Type getType()
    {
        if (getTransform().isSelection())
        {
            return getBase().getType();
        }
        else if (getTransform().isProjection() && getTransform().getExpr().isPresent())
        {
            final Type exprType = getTransform().getExpr().get().getType();

            if (exprType.equals(Type.UNDEFINED))
            {
                return exprType;
            }
            else
            {
                return Type.create(exprType.getName(), "*");
            }
        }
        else
        {
            return Type.UNDEFINED;
        }
    }

    static Query create(Expression expr, Transform transform)
    {
        return new QueryImpl(expr, transform);
    }
}

class QueryImpl implements Query
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final Expression base;
    private final Transform transform;

    QueryImpl(Expression base, Transform transform)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

        this.base = base;
        this.transform = transform;
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
        return "query";
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
