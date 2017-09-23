package cml.language.expressions;

import cml.language.types.NamedType;
import cml.language.types.Type;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class InvalidExpression extends ExpressionBase
{
    private final @Nullable String text;

    public InvalidExpression(final @Nullable String text)
    {
        this.text = text;
    }

    public Optional<String> getText()
    {
        return ofNullable(text);
    }

    @Override
    public String getKind()
    {
        return "invalid";
    }

    @Override
    public Type getType()
    {
        return NamedType.createUndefined(format("Unable to infer type of invalid expression: '%s'", text));
    }

    @Override
    public String toString()
    {
        return text;
    }
}
