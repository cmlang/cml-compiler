package cml.language.foundation;

import cml.language.expressions.Expression;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface Property extends TypedElement, Scope
{
    Optional<Expression> getValue();

    static Property create(String name, @Nullable Expression value, @Nullable Type type)
    {
        return new PropertyImpl(name, value, type);
    }
}

class PropertyImpl implements Property
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final TypedElement typedElement;
    private final Scope scope;
    private final @Nullable Expression value;

    PropertyImpl(String name, @Nullable Expression value, @Nullable Type type)
    {
        modelElement = ModelElement.create(this);
        namedElement = NamedElement.create(modelElement, name);
        typedElement = TypedElement.create(namedElement, type);
        scope = Scope.create(this, modelElement);

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
        return typedElement.isTypeRequired();
    }

    @Override
    public void setTypeRequired(boolean typeRequired)
    {
        typedElement.setTypeRequired(typeRequired);
    }

    @Override
    public boolean isTypeAllowed()
    {
        return typedElement.isTypeAllowed();
    }

    @Override
    public void setTypeAllowed(boolean typeAllowed)
    {
        typedElement.setTypeAllowed(true);
    }

    @Override
    public Optional<Type> getType()
    {
        return typedElement.getType();
    }

    @Override
    public List<ModelElement> getElements()
    {
        return scope.getElements();
    }

    @Override
    public void addElement(ModelElement element)
    {
        scope.addElement(element);
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

