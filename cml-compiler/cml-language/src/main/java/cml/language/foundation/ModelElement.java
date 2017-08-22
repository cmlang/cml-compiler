package cml.language.foundation;

import cml.language.loader.ModelVisitor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.util.Optional.empty;

public interface ModelElement
{
    Optional<Location> getLocation();
    void setLocation(@Nullable Location location);
    
    Optional<Scope> getParentScope();

    default <T> Optional<T> getSiblingNamed(String name, Class<T> clazz)
    {
        if (getParentScope().isPresent())
        {
            return getParentScope().get().getMemberNamed(name, clazz);
        }
        else
        {
            return empty();
        }
    }

    default void visit(ModelVisitor visitor)
    {
        throw new UnsupportedOperationException("visit() in: " + this);
    }

    static ModelElement create(ModelElement self)
    {
        return new ModelElementImpl(self);
    }
}

class ModelElementImpl implements ModelElement
{
    private final ModelElement self;

    private @Nullable Location location;

    ModelElementImpl(ModelElement self)
    {
        this.self = self;
    }

    @Override
    public Optional<Location> getLocation()
    {
        return Optional.ofNullable(location);
    }

    @Override
    public void setLocation(@Nullable Location location)
    {
        this.location = location;
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return scopeElement.getParentScope(self);
    }

    private static ScopeElement scopeElement;

    static void setScopeElement(ScopeElement association)
    {
        assert scopeElement == null;

        scopeElement = association;
    }

    static
    {
        ScopeElement.init(ModelElementImpl.class);
    }
}

