package cml.language.expressions;

import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface Assignment extends Expression
{
    String getVariable();
    Expression getValue();

    @Override
    default Optional<Type> getTypeOfVariableNamed(String name)
    {
        if (name.equals(getVariable())) return Optional.of(getType());

        return Expression.super.getTypeOfVariableNamed(name);
    }

    static Assignment create(String variable, Expression value)
    {
        return new AssignmentImpl(variable, value);
    }
}

class AssignmentImpl implements Assignment
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final String variable;
    private final Expression value;

    public AssignmentImpl(String variable, Expression value)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

        this.variable = variable;
        this.value = value;
    }

    @Override
    public String getVariable()
    {
        return variable;
    }

    @Override
    public Expression getValue()
    {
        return value;
    }

    @Override
    public String getKind()
    {
        return "assignment";
    }

    @Override
    public Type getType()
    {
        return value.getType();
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