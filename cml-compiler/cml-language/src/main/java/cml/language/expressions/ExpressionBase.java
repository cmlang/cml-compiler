package cml.language.expressions;

import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class ExpressionBase implements Expression
{
    private final ModelElement modelElement;
    private final Scope scope;

    ExpressionBase()
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);
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
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }

    @Override
    public void addMember(ModelElement member)
    {
        scope.addMember(member);
    }
}
