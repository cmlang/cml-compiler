package cml.language.types;

import cml.language.foundation.NamedElementBase;

import static java.lang.String.format;

public class MemberType extends NamedElementBase implements Type
{
    private final Type baseType;
    private final long paramIndex;

    public MemberType(final Type baseType, final String name, final long paramIndex)
    {
        super(name);
        this.baseType = baseType;
        this.paramIndex = paramIndex;
    }

    public Type getBaseType()
    {
        return baseType;
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
    public boolean isElementTypeAssignableFrom(final Type otherElementType)
    {
        return baseType.isElementTypeAssignableFrom(otherElementType);
    }

    @Override
    public String toString()
    {
        return format("%s.%s", baseType, getName());
    }
}
