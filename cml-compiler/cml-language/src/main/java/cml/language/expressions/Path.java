package cml.language.expressions;

import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public interface Path extends Expression
{
    Optional<Path> getBase();
    String getName();

    default List<String> getNames()
    {
        List<String> names = new LinkedList<>();
        names.add(getName());

        Optional<Path> path = getBase();
        while (path.isPresent())
        {
            names.add(0, path.get().getName());

            path = path.get().getBase();
        }

        return unmodifiableList(names);
    }

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
        assert getNames().size() >= 1: "In order to be able to determine its type, path must have at least one name.";
        assert getParentScope().isPresent(): "In order to be able to determine its type, path must be bound to a scope: " + getNames();

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
        Path path = null;

        for (String name: names)
        {
            path = new PathImpl(path, name);
        }

        return path;
    }
}

class PathImpl implements Path
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final @Nullable Path base;
    private final String name;

    PathImpl(@Nullable Path base, String name)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

        this.base = base;
        this.name = name;
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
    public Optional<Path> getBase()
    {
        return Optional.ofNullable(base);
    }

    @Override
    public String getName()
    {
        return name;
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

    @Override
    public String toString()
    {
        return getNames().stream().collect(joining("."));
    }
}
