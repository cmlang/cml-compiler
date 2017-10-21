package cml.language.types;

import cml.language.features.TempConcept;
import cml.language.generated.Concept;
import cml.language.generated.Element;
import cml.language.generated.Type;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static cml.language.functions.TypeFunctions.withCardinality;
import static cml.language.generated.Element.extendElement;
import static cml.language.generated.Type.extendType;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;

public interface TempNamedType extends Type
{
    TempNamedType UNDEFINED = TempNamedType.create("UNDEFINED");
    TempNamedType NOTHING = TempNamedType.create("NOTHING", "?");
    TempNamedType BOOLEAN = TempNamedType.create("BOOLEAN");
    TempNamedType STRING = TempNamedType.create("STRING");

    Collection<String> PRIMITIVE_TYPE_NAMES = unmodifiableCollection(asList(
        "BOOLEAN", "INTEGER", "DECIMAL", "STRING", "REGEX", // main primitive types
        "BYTE", "SHORT", "LONG", "FLOAT", "DOUBLE", "CHAR" // remaining primitive types
    ));

    List<String> NUMERIC_TYPE_NAMES = unmodifiableList(asList(
        "BYTE", "SHORT", "INTEGER", "LONG", "DECIMAL" // from narrower to wider
    ));

    List<String> BINARY_FLOATING_POINT_TYPE_NAMES = unmodifiableList(asList(
        "FLOAT", "DOUBLE" // from narrower to wider
    ));

    String getName();

    @Override
    default boolean isPrimitive()
    {
        return PRIMITIVE_TYPE_NAMES.contains(getName().toUpperCase());
    }

    @Override
    default boolean isBoolean()
    {
        return BOOLEAN.getName().equalsIgnoreCase(getName());
    }

    @Override
    default boolean isString()
    {
        return STRING.getName().equalsIgnoreCase(getName());
    }

    @Override
    default Type getInferredType()
    {
        return withCardinality(this, getInferredCardinality());
    }

    @Override
    default boolean isNumeric()
    {
        return NUMERIC_TYPE_NAMES.contains(getName().toUpperCase());
    }

    @Override
    default boolean isBinaryFloatingPoint()
    {
        return BINARY_FLOATING_POINT_TYPE_NAMES.contains(getName().toUpperCase());
    }

    @Override
    default boolean isUndefined()
    {
        return getName().toUpperCase().equals(UNDEFINED.getName());
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
    private final String cardinality;

    private Concept concept;

    NamedTypeImpl(String name, String cardinality)
    {
        final Element element = extendElement(this);

        this.type = extendType(this, element);

        this.name = name;
        this.cardinality = cardinality;
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
    public boolean isSomething()
    {
        return type.isSomething();
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
        return cardinality;
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

