package cml.language.types;

import cml.language.features.TempConcept;
import cml.language.generated.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Type.extendType;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;

public interface TempNamedType extends NamedType
{
    TempNamedType UNDEFINED = TempNamedType.create("UNDEFINED");
    TempNamedType NOTHING = TempNamedType.create("NOTHING");
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
        return new NamedTypeImpl(name, null, null);
    }

    static TempNamedType create(String name, @Nullable String cardinality)
    {
        return new NamedTypeImpl(name, cardinality, null);
    }

    static TempNamedType createUndefined(String errorMessage)
    {
        return new NamedTypeImpl(TempNamedType.UNDEFINED.getName(), null, errorMessage);
    }
}

class NamedTypeImpl implements TempNamedType
{
    private final NamedElement namedElement;
    private final Type type;

    private final @Nullable String cardinality;

    private @Nullable Concept concept;

    NamedTypeImpl(String name, @Nullable String cardinality, @Nullable String errorMessage)
    {
        final ModelElement modelElement = extendModelElement(this, null, null);

        this.namedElement = extendNamedElement(this, modelElement, name);
        this.type = extendType(this, modelElement, errorMessage);

        this.cardinality = cardinality;
    }

    @Override
    public Optional<Location> getLocation()
    {
        return type.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return type.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return type.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return type.getModule();
    }

    public Optional<String> getErrorMessage()
    {
        return type.getErrorMessage();
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
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public Optional<String> getCardinality()
    {
        return Optional.ofNullable(cardinality);
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
    public String toString()
    {
        return isUndefined() ? getName() : getName() + (getCardinality().isPresent() ? getCardinality().get() : "");
    }
}

