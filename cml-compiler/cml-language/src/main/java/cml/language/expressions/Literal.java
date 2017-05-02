package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Literal extends Expression
{
    String getText();

    static Literal create(String text, @Nullable Type type)
    {
        return new LiteralImpl(text, type);
    }
}

class LiteralImpl implements Literal
{
    private final ModelElement modelElement;
    private final Expression expression;
    private final String text;

    LiteralImpl(String text, @Nullable Type type)
    {
        this.modelElement = ModelElement.create(this);
        this.expression = Expression.create(modelElement, "literal", type);
        this.text = text;
    }

    @Override
    public String getText()
    {
        return this.text;
    }

    @Override
    public String getKind()
    {
        return expression.getKind();
    }

    @Override
    public Optional<Type> getType()
    {
        return expression.getType();
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
