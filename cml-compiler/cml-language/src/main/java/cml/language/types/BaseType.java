package cml.language.types;

import cml.language.foundation.ModelElementBase;
import cml.language.generated.Concept;
import cml.language.generated.Type;

import java.util.Optional;

public class BaseType extends ModelElementBase implements Type
{
    private final Type type;

    public BaseType()
    {
        this.type = Type.extendType(this, modelElement, null);
    }

    @Override
    public Optional<String> getCardinality()
    {
        return Optional.empty();
    }

    @Override
    public Optional<Concept> getConcept()
    {
        return Optional.empty();
    }

    @Override
    public String getKind()
    {
        return type.getKind();
    }

    @Override
    public Type getMatchingResultType()
    {
        return type.getMatchingResultType();
    }

    @Override
    public Type getBaseType()
    {
        return type.getBaseType();
    }

    @Override
    public boolean isParameter()
    {
        return type.isParameter();
    }

    @Override
    public boolean isDefined()
    {
        return type.isDefined();
    }

    @Override
    public boolean isUndefined()
    {
        return type.isUndefined();
    }

    @Override
    public boolean isSomething()
    {
        return type.isSomething();
    }

    @Override
    public boolean isNothing()
    {
        return type.isNothing();
    }

    @Override
    public boolean isRelational()
    {
        return type.isRelational();
    }

    @Override
    public boolean isReferential()
    {
        return type.isReferential();
    }

    @Override
    public boolean isPrimitive()
    {
        return type.isPrimitive();
    }

    @Override
    public boolean isNumeric()
    {
        return type.isNumeric();
    }

    @Override
    public boolean isBinaryFloatingPoint()
    {
        return type.isBinaryFloatingPoint();
    }

    @Override
    public boolean isBoolean()
    {
        return type.isBoolean();
    }

    @Override
    public Optional<String> getErrorMessage()
    {
        return type.getErrorMessage();
    }

    @Override
    public Type getElementType()
    {
        return type.getElementType();
    }

    @Override
    public boolean isString()
    {
        return type.isString();
    }

    @Override
    public boolean isSingle()
    {
        return type.isSingle();
    }

    @Override
    public boolean isRequired()
    {
        return type.isRequired();
    }

    @Override
    public boolean isOptional()
    {
        return type.isOptional();
    }

    @Override
    public boolean isSequence()
    {
        return type.isSequence();
    }
}
