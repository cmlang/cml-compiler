package cml.language.expressions;

import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.types.NamedType;
import cml.language.types.Type;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableCollection;

public interface Unary extends Expression
{
    String getOperator();
    Optional<String> getOperation();
    Expression getExpr();

    static Unary create(String operator, Expression expr)
    {
        return new UnaryImpl(operator, expr);
    }
}

class UnaryImpl implements Unary
{
    private final Collection<String> LOGIC_OPERATORS = unmodifiableCollection(singletonList(
        "not" // boolean operators
    ));

    private static Map<String, String> OPERATIONS =
        new HashMap<String, String>()
        {{
            put("+", "add");
            put("-", "sub");
            put("not", "not");
        }};

    private final ModelElement modelElement;
    private final Scope scope;

    private final String operator;
    private final Expression expr;

    UnaryImpl(String operator, Expression expr)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

        this.operator = operator;
        this.expr = expr;
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
    public String getOperator()
    {
        return operator;
    }

    @Override
    public Optional<String> getOperation()
    {
        final String operation = OPERATIONS.get(getOperator());

        return Optional.ofNullable(operation);
    }

    @Override
    public Expression getExpr()
    {
        return expr;
    }

    @Override
    public String getKind()
    {
        return "unary";
    }

    @Override
    public Type getType()
    {
        return LOGIC_OPERATORS.contains(getOperator()) ? NamedType.BOOLEAN : expr.getType();
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
}
