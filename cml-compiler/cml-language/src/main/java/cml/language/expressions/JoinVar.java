package cml.language.expressions;

import cml.language.foundation.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface JoinVar extends TypedElement
{
    Path getPath();

    default Type getType()
    {
        return getPath().getType();
    }

    static JoinVar create(String name, Path path)
    {
        return new JoinVarImpl(name, path);
    }
}

class JoinVarImpl implements JoinVar
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;

    private final Path path;

    JoinVarImpl(String name, Path path)
    {
        modelElement = ModelElement.create(this);
        namedElement = NamedElement.create(modelElement, name);

        this.path = path;
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
    public Path getPath()
    {
        return path;
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }
}
