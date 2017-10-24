package cml.language.expressions;

import cml.language.generated.*;
import cml.language.types.TempNamedType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableCollection;

public class TempInfix extends ExpressionBase implements Infix
{
    private static final String REFERENTIAL_EQUALITY = "===";
    private static final String REFERENTIAL_INEQUALITY = "!==";

    private static final Collection<String> REFERENTIAL_OPERATORS = unmodifiableCollection(asList(
        REFERENTIAL_EQUALITY, REFERENTIAL_INEQUALITY
    ));

    private static final Collection<String> STRING_OPERATORS = unmodifiableCollection(singletonList("&"));

    private static Map<String, String> OPERATIONS =
        new HashMap<String, String>()
        {{
            // String Concatenation:
            put("&", "str_concat");

            // Referential Operators:
            put(REFERENTIAL_EQUALITY, "ref_eq");
            put(REFERENTIAL_INEQUALITY, "not_ref_eq");
        }};

    private final Infix infix;

    public TempInfix(String operator, Expression left, Expression right)
    {
        super(asList(left, right));

        infix = Infix.extendInfix(this, expression, expression, expression, expression, operator);
    }

    @Override
    public String getOperator()
    {
        return infix.getOperator();
    }

    @Override
    public Optional<String> getOperation()
    {
        final String operation = OPERATIONS.get(getOperator());

        return Optional.ofNullable(operation);
    }

    @Override
    public Expression getLeft()
    {
        return infix.getLeft();
    }

    @Override
    public Expression getRight()
    {
        return infix.getRight();
    }

    @Override
    public String getKind()
    {
        return "infix";
    }

    @Override
    public Type getType()
    {
        return infix.getType();
    }

    @Override
    public Type getInferredType()
    {
        final Type leftType = getLeft().getType();
        final Type rightType = getRight().getType();

        assert leftType != null: "Left expression must have a type in order to be able to compute type of infix expression: " + getLeft().getKind();
        assert rightType != null: "Right expression must have a type in order to be able to compute type of infix expression: " + getRight().getKind();

        if (REFERENTIAL_OPERATORS.contains(getOperator()) && leftType.isReferential() && rightType.isReferential())
        {
            return TempNamedType.BOOLEAN;
        }
        else if (STRING_OPERATORS.contains(getOperator()) && leftType.isPrimitive() && rightType.isPrimitive())
        {
            return TempNamedType.STRING;
        }
        else
        {
            return getUndefinedType();
        }
    }

    @Override
    public String getDiagnosticId()
    {
        return infix.getDiagnosticId();
    }

    @Override
    public ValueType getBooleanType()
    {
        return infix.getBooleanType();
    }

    @Override
    public UndefinedType getUndefinedType()
    {
        return infix.getUndefinedType();
    }
}
