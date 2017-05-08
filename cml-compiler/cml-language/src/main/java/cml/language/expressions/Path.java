package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface Path extends Expression
{
    List<String> getNames();

    default List<String> getMemberNames()
    {
        assert getNames().size() >= 1: "Path must have at least one name in order to determine its member names.";

        return getNames().stream().skip(1).collect(toList());
    }

    default Scope getContext()
    {
        final Type type = getType();
        if (type.equals(Type.UNDEFINED)) return this;

        final Optional<Scope> typeScope = getScopeOfType(type);
        return typeScope.orElse(this);
    }

    default Type getType()
    {
        assert getNames().size() >= 1: "Path must have at least one name in order to determine its type.";
        assert getParentScope().isPresent(): "Path must be bound to a scope in order to determine its type: " + getNames();

        Scope scope = getParentScope().get();

        if (isSelf()) return scope.getSelfType();

        final String variableName = getNames().get(0);
        final Optional<Type> variableType = scope.getTypeOfElementNamed(variableName);

        StringBuilder intermediatePath = new StringBuilder(variableName);

        if (variableType.isPresent())
        {
            Type type = variableType.get();

            for (final String memberName: getMemberNames())
            {
                intermediatePath.append(".").append(memberName);

                final Optional<Scope> optionalScope = scope.getScopeOfType(type);

                if (optionalScope.isPresent())
                {
                    scope = optionalScope.get();

                    final Optional<Type> memberType = scope.getTypeOfMemberNamed(memberName);

                    if (memberType.isPresent())
                    {
                        type = memberType.get();
                    }
                    else
                    {
                        return Type.createUndefined("Unable to find type of member: " + intermediatePath);
                    }
                }
                else
                {
                    return Type.createUndefined("Unable to find type: " + type.getName());
                }
            }

            return type;
        }
        else
        {
            return Type.createUndefined("Unable to find type of variable: " + intermediatePath);
        }
    }

    default boolean isSelf()
    {
        return getNames().size() == 1 && getNames().get(0).equals("self");
    }

    static Path create(List<String> names)
    {
        return new PathImpl(names);
    }
}

class PathImpl implements Path
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final List<String> names;

    PathImpl(List<String> names)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

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
        return "path";
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
