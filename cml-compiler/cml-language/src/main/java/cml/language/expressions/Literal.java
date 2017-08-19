package cml.language.expressions;

import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.types.NamedType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface Literal extends Expression
{
    String getText();

    static Literal create(String text, NamedType type)
    {
        return new LiteralImpl(text, type);
    }
}

class LiteralImpl implements Literal
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final String text;
    private final NamedType type;

    LiteralImpl(String text, NamedType type)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

        this.text = text;
        this.type = type;
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
    public String getText()
    {
        return this.text;
    }

    @Override
    public NamedType getType()
    {
        return type;
    }

    @Override
    public String getKind()
    {
        return "literal";
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

    @Override
    public String toString()
    {
        return text;
    }
}
