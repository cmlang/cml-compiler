package cml.language.expressions;

import cml.language.generated.Scope;
import cml.language.types.NamedType;
import cml.language.types.Type;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static cml.language.functions.ModelElementFunctions.selfTypeOf;
import static cml.language.functions.ScopeFunctions.*;
import static cml.language.functions.TypeFunctions.withCardinality;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;

public class Path extends ExpressionBase
{
    private final @Nullable Path base;
    private final String name;

    public static Path create(List<String> names)
    {
        Path path = null;

        for (String name: names)
        {
            final Path base = path;

            path = new Path(base, name);
        }

        return path;
    }

    public Path(@Nullable Path base, String name)
    {
        super(singletonList(base));

        this.base = base;
        this.name = name;
    }

    public List<String> getNames()
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

    public List<String> getMemberNames()
    {
        assert getNames().size() >= 1: "Path must have at least one name in order to determine its member names.";

        return getNames().stream().skip(1).collect(toList());
    }

    public boolean isFirst()
    {
        return !getBase().isPresent();
    }

    public boolean isLast()
    {
        return !getParent().isPresent() || !(getParent().get() instanceof Path);
    }

    @Override
    public Type getType()
    {
        assert getNames().size() >= 1: "In order to be able to determine its type, path must have at least one name.";
        assert getParent().isPresent(): "In order to be able to determine its type, path must be bound to a scope: " + getNames() + " " + getLocation();

        Scope scope = getParent().get();

        if (isSelf()) return selfTypeOf(scope);

        final String variableName = getNames().get(0);
        final Optional<Type> variableType = typeOfVariableNamed(variableName, scope);

        StringBuilder intermediatePath = new StringBuilder(variableName);

        if (variableType.isPresent())
        {
            Type type = variableType.get();

            for (final String memberName: getMemberNames())
            {
                intermediatePath.append(".").append(memberName);

                final Optional<Scope> optionalScope = scopeOfType(type, scope);

                if (optionalScope.isPresent())
                {
                    scope = optionalScope.get();

                    final Optional<Type> memberType = typeOfMemberNamed(memberName, scope);

                    if (memberType.isPresent())
                    {
                        final String cardinality = memberType.get().getCardinality().orElse(null);

                        type = withCardinality(memberType.get(), type.isSequence() ? "*" : cardinality);
                    }
                    else
                    {
                        return NamedType.createUndefined("Unable to find type of member: " + intermediatePath);
                    }
                }
                else
                {
                    return NamedType.createUndefined("Unable to find type: " + type);
                }
            }

            return type;
        }
        else
        {
            return NamedType.createUndefined("Unable to find type of variable: " + intermediatePath);
        }
    }

    public Type getElementType()
    {
        return getType().getElementType();
    }

    public boolean isSelf()
    {
        return getNames().size() == 1 && getNames().get(0).equals("self");
    }

    public Optional<Path> getBase()
    {
        return Optional.ofNullable(base);
    }

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
    public String toString()
    {
        return getNames().stream().collect(joining("."));
    }
}
