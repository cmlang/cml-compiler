package cml.language.expressions;

import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.Scope;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.jooq.lambda.Seq.seq;

public abstract class ExpressionBase implements Expression
{
    private final ModelElement modelElement;
    private final Scope scope;

    ExpressionBase()
    {
        this(null, emptyList());
    }

    ExpressionBase(Scope scope)
    {
        this(scope, emptyList());
    }

    ExpressionBase(List<Expression> subExpressions)
    {
        this(null, subExpressions);
    }

    ExpressionBase(Scope parent, List<Expression> subExpressions)
    {
        modelElement = ModelElement.extendModelElement(this, parent, null);
        scope = Scope.extendScope(this, modelElement, seq(subExpressions).map(s -> (ModelElement)s).toList());
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return modelElement.getParent();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }
}
