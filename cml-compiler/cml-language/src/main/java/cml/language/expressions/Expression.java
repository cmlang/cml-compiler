package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface Expression extends ModelElement
{
    String getKind();
    Optional<Type> getType();

    static Expression create(ModelElement modelElement, String kind, @Nullable Type type)
    {
        return new ExpressionImpl(modelElement, kind, type);
    }
}

class ExpressionImpl implements Expression
{
    private final ModelElement modelElement;
    private final String kind;
    private final @Nullable Type type;

    ExpressionImpl(ModelElement modelElement, String kind, @Nullable Type type)
    {
        this.modelElement = modelElement;
        this.kind = kind;
        this.type = type;
    }

    @Override
    public String getKind()
    {
        return kind;
    }

    @Override
    public Optional<Type> getType()
    {
        return Optional.ofNullable(type);
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }
}
