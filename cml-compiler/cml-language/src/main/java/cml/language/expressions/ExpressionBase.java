package cml.language.expressions;

import cml.language.generated.*;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.jooq.lambda.Seq.seq;

public abstract class ExpressionBase implements Expression
{
    protected final Expression expression;

    ExpressionBase()
    {
        this(null, emptyList());
    }

    ExpressionBase(Scope scope)
    {
        this(scope, emptyList());
    }

    ExpressionBase(List<Expression> operands)
    {
        this(null, operands);
    }

    ExpressionBase(Scope parent, List<Expression> operands)
    {
        final Element element = Element.extendElement(this);
        final ModelElement modelElement = ModelElement.extendModelElement(this, element, parent, null);
        final Scope scope = Scope.extendScope(this, element, modelElement, seq(operands).map(s -> (ModelElement)s).toList());

        expression = Expression.extendExpression(this, element, modelElement, scope);
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

    @Override
    public String getKind()
    {
        return expression.getKind();
    }

    @Override
    public Type getType()
    {
        return expression.getType();
    }

    @Override
    public Type getMatchingResultType()
    {
        return expression.getMatchingResultType();
    }

    @Override
    public boolean isNumeric()
    {
        return expression.isNumeric();
    }

    @Override
    public boolean isBinaryFloatingPoint()
    {
        return expression.isBinaryFloatingPoint();
    }

    @Override
    public boolean isArithmetic()
    {
        return expression.isArithmetic();
    }

    @Override
    public List<Expression> getOperands()
    {
        return expression.getOperands();
    }
}
