package cml.language.types;

import cml.language.generated.Type;

import static java.lang.String.format;

public class MemberType extends BaseType
{
    private final Type baseType;
    private final String name;
    private final long paramIndex;

    public MemberType(final Type baseType, final String name, final long paramIndex)
    {
        this.baseType = baseType;
        this.name = name;
        this.paramIndex = paramIndex;
    }

    public Type getBaseType()
    {
        return baseType;
    }

    public String getName()
    {
        return name;
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
    public String getDiagnosticId()
    {
        return format("%s.%s", baseType, getName());
    }
}
