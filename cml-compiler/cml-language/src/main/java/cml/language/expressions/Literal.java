package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;

import java.util.List;
import java.util.Optional;

public interface Literal extends Expression
{
    String getText();

    static Literal create(String text, Type type)
    {
        return new LiteralImpl(text, type);
    }
}

class LiteralImpl implements Literal
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final String text;
    private final Type type;

    LiteralImpl(String text, Type type)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

        this.text = text;
        this.type = type;
    }

    @Override
    public String getText()
    {
        return this.text;
    }

    @Override
    public Type getType()
    {
        return type;
    }

    @Override
    public String getKind()
    {
        return "literal";
    }

    @Override
    public void addElement(ModelElement element)
    {
        scope.addElement(element);
    }

    @Override
    public List<ModelElement> getElements()
    {
        return scope.getElements();
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }

    @Override
    public String toString()
    {
        return text;
    }
}
