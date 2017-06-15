package cml.language.foundation;

import cml.language.features.Concept;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;

public interface Type extends NamedElement
{
    Type UNDEFINED = Type.create("Undefined");
    Type BOOLEAN = Type.create("Boolean");
    Type STRING = Type.create("String");

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

    default boolean isParameter() { return isDefined() && !isPrimitive() && !isConcept(); }

    default boolean isConcept() { return getConcept().isPresent(); }

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
        assert this.isNumeric() && other.isNumeric()
            : "Both types must be numeric in order to be compared: " + this.getName() + " & " + other.getName();

        return NUMERIC_TYPE_NAMES.indexOf(this.getName()) > NUMERIC_TYPE_NAMES.indexOf(other.getName());
    }

    default boolean isBinaryFloatingPointWiderThan(Type other)
    {
        assert this.isBinaryFloatingPoint() && other.isBinaryFloatingPoint()
            : "Both types must be binary floating-point in order to be compared: " + this.getName() + " & " + other.getName();

        return BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(this.getName()) > BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(other.getName());
    }

    Optional<String> getCardinality();

    Optional<String> getErrorMessage();

    Optional<Concept> getConcept();
    void setConcept(@Nullable Concept concept);

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

    default boolean isTypeAssignableFrom(Type other)
    {
        if (this.getName().equals(other.getName()))
        {
            return true;
        }
        else if (this.isNumeric() && other.isNumeric())
        {
            return this.isNumericWiderThan(other);
        }
        else if (this.isBinaryFloatingPoint() && other.isBinaryFloatingPoint())
        {
            return this.isBinaryFloatingPointWiderThan(other);
        }
        else if (this.getConcept().isPresent() && other.getConcept().isPresent())
        {
            return other.getConcept()
                        .get()
                        .getAllGeneralizations()
                        .stream()
                        .anyMatch(c -> c.getName().equals(this.getName()));
        }
        else
        {
            return false;
        }
    }

    default boolean isCardinalityAssignableFrom(Type other)
    {
        return Objects.equals(this.getCardinality(), other.getCardinality()) ||
               (this.isOptional() && other.isRequired()) ||
               (this.isSequence());
    }

    default boolean isAssignableFrom(Type other)
    {
        return this.isTypeAssignableFrom(other) && this.isCardinalityAssignableFrom(other);
    }

    static Type create(String name)
    {
        return new TypeImpl(name, null, null);
    }

    static Type create(String name, @Nullable String cardinality)
    {
        return new TypeImpl(name, cardinality, null);
    }

    static Type createUndefined(String errorMessage)
    {
        return new TypeImpl(Type.UNDEFINED.getName(), null, errorMessage);
    }
}

class TypeImpl implements Type
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final @Nullable String cardinality;
    private final @Nullable String errorMessage;

    private @Nullable Concept concept;

    TypeImpl(String name, @Nullable String cardinality, @Nullable String errorMessage)
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
        final TypeImpl other = (TypeImpl) o;
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

