package cml.language.types;

import cml.language.features.TempConcept;
import cml.language.generated.Concept;
import cml.language.generated.Element;
import cml.language.generated.Type;
import cml.language.generated.ValueType;
import cml.primitives.Types;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static cml.language.functions.TypeFunctions.withCardinality;
import static cml.language.generated.Element.extendElement;
import static cml.language.generated.Type.extendType;

public interface TempNamedType extends Type
{
    TempNamedType NOTHING = TempNamedType.create("NOTHING", "?");
    ValueType BOOLEAN = ValueType.createValueType("", Types.BOOLEAN);
    ValueType STRING = ValueType.createValueType("", Types.STRING);

    String getName();

    @Override
    default Type getInferredType()
    {
        return withCardinality(this, getInferredCardinality());
    }

    default boolean isNothing()
    {
        return getName().toUpperCase().equals(NOTHING.getName());
    }

    @Override
    default TempNamedType getElementType()
    {
        final TempNamedType elementType = TempNamedType.create(getName());

        getConcept().map(c -> (TempConcept)c).ifPresent(elementType::setConcept);

        return elementType;
    }

    void setConcept(@NotNull Concept concept);

    static TempNamedType create(String name)
    {
        return new NamedTypeImpl(name, "");
    }

    static TempNamedType create(String name, String cardinality)
    {
        return new NamedTypeImpl(name, cardinality);
    }
}

class NamedTypeImpl implements TempNamedType
{
    private final Type type;

    private final String name;

    private Concept concept;

    NamedTypeImpl(String name, String cardinality)
    {
        final Element element = extendElement(this);

        this.type = extendType(this, element, cardinality);

        this.name = name;
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
    public boolean isBoolean()
    {
        return type.isBoolean();
    }

    @Override
    public boolean isNumeric()
    {
        return type.isNumeric();
    }

    @Override
    public boolean isFloat()
    {
        return type.isFloat();
    }

    @Override
    public boolean isString()
    {
        return type.isString();
    }

    @Override
    public boolean isPrimitive()
    {
        return type.isPrimitive();
    }

    @Override
    public boolean isRelational()
    {
        return isNumeric() || isFloat() || isString();
    }

    @Override
    public boolean isReferential()
    {
        return type.isReferential();
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

    @Override
    public String getInferredCardinality()
    {
        return type.getInferredCardinality();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getCardinality()
    {
        return type.getCardinality();
    }

    @Override
    public int getMinCardinality()
    {
        return type.getMinCardinality();
    }

    @Override
    public int getMaxCardinality()
    {
        return type.getMaxCardinality();
    }

    @Override
    public Optional<Concept> getConcept()
    {
        return Optional.ofNullable(concept);
    }

    public void setConcept(@NotNull Concept concept)
    {
        assert this.concept == null;

        this.concept = concept;
    }

    @Override
    public String getDiagnosticId()
    {
        return isUndefined() ? getName() : getName() + getCardinality();
    }
}

