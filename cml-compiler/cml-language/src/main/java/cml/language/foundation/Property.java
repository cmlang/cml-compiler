package cml.language.foundation;

import cml.language.expressions.Expression;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public interface Property extends TypedElement, Scope
{
    Optional<Expression> getValue();

    boolean isTypeRequired();
    void setTypeRequired(boolean typeRequired);

    boolean isTypeAllowed();
    void setTypeAllowed(boolean typeAllowed);

    static Property create(String name, @Nullable Type type)
    {
        return new PropertyImpl(name, type, null);
    }

    static Property create(String name, @Nullable Expression value)
    {
        return new PropertyImpl(name, null, value);
    }

    static Property create(String name, @Nullable Type type, @Nullable Expression value)
    {
        return new PropertyImpl(name, type, value);
    }
}

class PropertyImpl implements Property
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    private boolean typeRequired;
    private boolean typeAllowed;

    private final @Nullable Type type;
    private final @Nullable Expression value;

    PropertyImpl(String name, @Nullable Type type, @Nullable Expression value)
    {
        modelElement = ModelElement.create(this);
        namedElement = NamedElement.create(modelElement, name);
        scope = Scope.create(this, modelElement);

        this.type = type;
        this.value = value;
    }

    @Override
    public Optional<Expression> getValue()
    {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean isTypeRequired()
    {
        return typeRequired;
    }

    @Override
    public void setTypeRequired(boolean typeRequired)
    {
        this.typeRequired = typeRequired;
    }

    @Override
    public boolean isTypeAllowed()
    {
        return typeAllowed;
    }

    @Override
    public void setTypeAllowed(boolean typeAllowed)
    {
        this.typeAllowed = typeAllowed;
    }

    @Override
    public Type getType()
    {
        if (type == null)
        {
            if (value == null)
            {
                return Type.createUndefined("No type defined for property: " + getName());
            }
            else
            {
                return value.getType();
            }
        }
        else
        {
            return type;
        }
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

    @Override
    public String toString()
    {
        if (getParentScope().isPresent() && getParentScope().get() instanceof NamedElement)
        {
            final NamedElement namedElement = (NamedElement)getParentScope().get();

            return format("property %s.%s: %s",  namedElement.getName(), getName(), getType());
        }
        else
        {
            return format("property %s: %s",  getName(), getType());
        }
    }
}

