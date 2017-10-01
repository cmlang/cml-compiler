package cml.language.types;

import cml.language.foundation.NamedElementBase;

import static java.lang.String.format;

public class MemberType extends NamedElementBase implements TempType
{
    private final TempType baseType;
    private final long paramIndex;

    public MemberType(final TempType baseType, final String name, final long paramIndex)
    {
        super(name);
        this.baseType = baseType;
        this.paramIndex = paramIndex;
    }

    public TempType getBaseType()
    {
        return baseType;
    }

    public long getParamIndex()
    {
        return paramIndex;
    }

    @Override
    public TempType getElementType()
    {
        assert !getCardinality().isPresent();

        return this;
    }

    @Override
    public String toString()
    {
        return format("%s.%s", baseType, getName());
    }
}
