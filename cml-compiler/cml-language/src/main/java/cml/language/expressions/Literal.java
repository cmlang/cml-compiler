package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;

import java.util.Optional;

public interface Literal extends ModelElement
{
    String getText();
    Type getType();

    static Literal create(String text, Type type)
    {
        return new LiteralImpl(text, type);
    }
}

class LiteralImpl implements Literal
{
    private final ModelElement modelElement;
    private final String text;
    private final Type type;

    LiteralImpl(String text, Type type)
    {
        this.modelElement = ModelElement.create(this);
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
        return this.type;
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
