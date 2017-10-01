package cml.language.expressions;

import cml.language.generated.*;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.jooq.lambda.Seq.seq;

public abstract class ExpressionBase implements TempExpression
{
    private final Expression expression;

    ExpressionBase()
    {
        this(null, emptyList());
    }

    ExpressionBase(Scope scope)
    {
        this(scope, emptyList());
    }

    ExpressionBase(List<TempExpression> subExpressions)
    {
        this(null, subExpressions);
    }

    ExpressionBase(Scope parent, List<TempExpression> subExpressions)
    {
        final ModelElement modelElement = ModelElement.extendModelElement(this, parent, null);
        final Scope scope = Scope.extendScope(this, modelElement, seq(subExpressions).map(s -> (ModelElement)s).toList());

        expression = Expression.extendExpression(this, modelElement, scope);
    }

    @Override
    public Optional<Location> getLocation()
    {
        return expression.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return expression.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return expression.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return expression.getModule();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return expression.getMembers();
    }
}
