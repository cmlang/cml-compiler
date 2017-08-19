package cml.language.types;

import cml.language.features.Concept;
import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.NamedElement;
import cml.language.foundation.Scope;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;

public interface NamedType extends Type, NamedElement
{
    NamedType UNDEFINED = NamedType.create("Undefined");
    NamedType BOOLEAN = NamedType.create("Boolean");
    NamedType STRING = NamedType.create("String");

    Collection<String> PRIMITIVE_TYPE_NAMES = unmodifiableCollection(asList(
        "Boolean", "Integer", "Decimal", "String", "Regex", // main primitive types
        "Byte", "Short", "Long", "Float", "Double", "Char" // remaining primitive types
    ));

    List<String> NUMERIC_TYPE_NAMES = unmodifiableList(asList(
        "Byte", "Short", "Integer", "Long", "Decimal" // from narrower to wider
    ));

    List<String> BINARY_FLOATING_POINT_TYPE_NAMES = unmodifiableList(asList(
        "Float", "Double" // from narrower to wider
    ));

    String REQUIRED = "required";
    String OPTIONAL = "optional";
    String SEQUENCE = "sequence";

    default boolean isPrimitive()
    {
        return PRIMITIVE_TYPE_NAMES.contains(getName());
    }

    default boolean isNumeric()
    {
        return NUMERIC_TYPE_NAMES.contains(getName());
    }

    default boolean isBinaryFloatingPoint()
    {
        return BINARY_FLOATING_POINT_TYPE_NAMES.contains(getName());
    }

    default boolean isDefined()
    {
        return !isUndefined();
    }

    default boolean isUndefined()
    {
        return getName().equals(UNDEFINED.getName());
    }

    default boolean isOptional()
    {
        return getKind().equals(OPTIONAL);
    }

    default boolean isRequired()
    {
        return getKind().equals(REQUIRED);
    }

    default boolean isSequence()
    {
        return getKind().equals(SEQUENCE);
    }

    default boolean isNumericWiderThan(Type other)
    {
        if (other instanceof NamedType)
        {
            final NamedType otherType = (NamedType) other;

            assert this.isNumeric() && other.isNumeric()
                : "Both types must be numeric in order to be compared: " + this.getName() + " & " + otherType.getName();

            return NUMERIC_TYPE_NAMES.indexOf(this.getName()) > NUMERIC_TYPE_NAMES.indexOf(otherType.getName());
        }

        return false;
    }

    default boolean isBinaryFloatingPointWiderThan(Type other)
    {
        if (other instanceof NamedType)
        {
            final NamedType otherType = (NamedType) other;

            assert this.isBinaryFloatingPoint() && other.isBinaryFloatingPoint()
                : "Both types must be binary floating-point in order to be compared: " + this.getName() + " & " + otherType.getName();

            return BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(this.getName()) > BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(otherType.getName());
        }

        return false;
    }

    default String getKind()
    {
        if (getCardinality().isPresent())
        {
            final String cardinality = getCardinality().get();

            if (cardinality.matches("\\?"))
            {
                return OPTIONAL;
            }
            else if (cardinality.matches("([*+])"))
            {
                return SEQUENCE;
            }
        }

        return REQUIRED;
    }

    default NamedType getElementType()
    {
        final NamedType elementType = NamedType.create(getName());

        if (getConcept().isPresent()) elementType.setConcept(getConcept().get());

        return elementType;
    }

    @Override
    default Type withCardinality(String cardinality)
    {
        return NamedType.create(getName(), cardinality);
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

    private @Nullable Concept concept;

    NamedTypeImpl(String name, @Nullable String cardinality, @Nullable String errorMessage)
    {
        this.modelElement = ModelElement.create(this);
        this.namedElement = NamedElement.create(modelElement, name);
        this.cardinality = cardinality;
        this.errorMessage = errorMessage;
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public void setLocation(@Nullable Location location)
    {
        modelElement.setLocation(location);
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
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
    public Optional<Concept> getConcept()
    {
        return Optional.ofNullable(concept);
    }

    @Override
    public void setConcept(@Nullable Concept concept)
    {
        this.concept = concept;
    }

    @Override
    public String toString()
    {
        return getName() + (getCardinality().isPresent() ? getCardinality().get() : "");
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NamedTypeImpl other = (NamedTypeImpl) o;
        return
            Objects.equals(this.getName(), other.getName()) &&
            Objects.equals(this.cardinality, other.cardinality);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(cardinality);
    }
}

