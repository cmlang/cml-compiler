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
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;

public interface NamedType extends TempType, NamedElement
{
    NamedType UNDEFINED = NamedType.create("UNDEFINED");
    NamedType BOOLEAN = NamedType.create("BOOLEAN");
    NamedType STRING = NamedType.create("STRING");

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
    default boolean isDefined()
    {
        return !isUndefined();
    }

    @Override
    default boolean isUndefined()
    {
        return getName().toUpperCase().equals(UNDEFINED.getName());
    }

    @Override
    default NamedType getElementType()
    {
        final NamedType elementType = NamedType.create(getName());

        getConcept().ifPresent(elementType::setConcept);

        return elementType;
    }

    static NamedType create(String name)
    {
        return new NamedTypeImpl(name, null, null);
    }

    static NamedType create(String name, @Nullable String cardinality)
    {
        return new NamedTypeImpl(name, cardinality, null);
    }

    static NamedType createUndefined(String errorMessage)
    {
        return new NamedTypeImpl(NamedType.UNDEFINED.getName(), null, errorMessage);
    }
}

class NamedTypeImpl implements NamedType
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final @Nullable String cardinality;
    private final @Nullable String errorMessage;

    private @Nullable TempConcept concept;

    NamedTypeImpl(String name, @Nullable String cardinality, @Nullable String errorMessage)
    {
        this.modelElement = extendModelElement(this, null, null);
        this.namedElement = extendNamedElement(this, modelElement, name);
        this.cardinality = cardinality;
        this.errorMessage = errorMessage;
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return modelElement.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return modelElement.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return modelElement.getModule();
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

    public Optional<String> getErrorMessage()
    {
        return Optional.ofNullable(errorMessage);
    }

    @Override
    public Optional<TempConcept> getConcept()
    {
        return Optional.ofNullable(concept);
    }

    @Override
    public void setConcept(@NotNull TempConcept concept)
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

