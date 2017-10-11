package cml.language.types;

import cml.language.generated.NamedElement;
import cml.language.generated.Type;

import java.util.Optional;

import static java.lang.String.format;

public class MemberType extends BaseType implements NamedElement
{
    private final NamedElement namedElement;

    private final Type baseType;
    private final long paramIndex;

    public MemberType(final Type baseType, final String name, final long paramIndex)
    {
        namedElement = NamedElement.extendNamedElement(this, modelElement, name);

        this.baseType = baseType;
        this.paramIndex = paramIndex;
    }

    public Type getBaseType()
    {
        return baseType;
    }

    @Override
    public boolean isString()
    {
        return false;
    }

    public long getParamIndex()
    {
        return paramIndex;
    }

    @Override
    public Type getElementType()
    {
        assert !getCardinality().isPresent();

        return this;
    }

    @Override
    public String getKind()
    {
        return "member";
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public String toString()
    {
        return format("%s.%s", baseType, getName());
    }
}
