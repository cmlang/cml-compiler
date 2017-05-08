package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import cml.language.foundation.TypedElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface Path extends Expression
{
    List<String> getNames();

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
    public Type getType()
    {
        assert getParentScope().isPresent(): "Path must be bound to a scope in order to determine its type: " + getNames();

        Scope scope = getParentScope().get();

        if (isSelf()) return scope.getSelfType();

        Type type = Type.UNDEFINED;

        for (String propertyName: getNames())
        {
            scope = getNextScope(scope, type.getName());

            final Optional<TypedElement> property = scope.getElementNamed(propertyName, TypedElement.class);
            if (property.isPresent())
            {
                final Optional<Type> optionalType = property.get().getType();

                assert optionalType.isPresent(): "Undefined type of element: " + property.get().getName();

                if (type.getCardinality().isPresent())
                {
                    // New type with the previous type's cardinality:
                    type = Type.create(
                                optionalType.get().getName(),
                                type.getCardinality().get());
                }
                else
                {
                    type = optionalType.get();
                }
            }
        }
        
        return type;
    }

    private boolean isSelf()
    {
        return getNames().size() == 1 && getNames().get(0).equals("self");
    }

    @NotNull
    private Scope getNextScope(Scope scope, String typeName)
    {
        if (typeName.equals(Type.UNDEFINED.getName()))
        {
            final Optional<Path> optionalPath = getParentScope(Path.class);

            if (optionalPath.isPresent())
            {
                scope = getScope(scope, optionalPath.get().getType().getName());
            }
        }
        else
        {
            scope = getScope(scope, typeName);
        }

        return scope;
    }

    private Scope getScope(Scope scope, String typeName)
    {
        final Optional<Scope> optionalScope = scope.getElementNamed(typeName, Scope.class);

        assert optionalScope.isPresent(): "Undefined scope: " + typeName;

        return optionalScope.get();
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
