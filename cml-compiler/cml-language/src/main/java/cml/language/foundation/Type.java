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

    Collection<String> PRIMITIVE_TYPE_NAMES = unmodifiableCollection(asList(
        "Boolean", "Integer", "Decimal", "String", "Regex", // main primitive types
        "Byte", "Short", "Long", "Float", "Double", "Char" // remaining primitive types
    ));

    List<String> ORDINAL_TYPE_NAMES = unmodifiableList(asList(
        "Byte", "Short", "Integer", "Long", "Float", "Double", "Decimal" // from smaller to largest
    ));

    String REQUIRED = "required";
    String OPTIONAL = "optional";
    String SET = "set";

    default boolean isPrimitive()
    {
        return PRIMITIVE_TYPE_NAMES.contains(getName());
    }

    default boolean isOrdinal()
    {
        return ORDINAL_TYPE_NAMES.contains(getName());
    }

    default boolean isDefined()
    {
        return !isUndefined();
    }

    default boolean isUndefined()
    {
        return getName().equals(UNDEFINED.getName());
    }

    default boolean isGreaterThan(Type other)
    {
        assert this.isOrdinal() && other.isOrdinal()
            : "Both types must be ordinal in order to be compared: " + this.getName() + " & " + other.getName();

        return ORDINAL_TYPE_NAMES.indexOf(this.getName()) > ORDINAL_TYPE_NAMES.indexOf(other.getName());
    }

    Optional<String> getCardinality();

    Optional<String> getErrorMessage();

    Optional<Concept> getConcept();
    void setConcept(Concept module);

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
                return SET;
            }
        }

        return REQUIRED;
    }

    static Type create(String name)
    {
        return new TypeImpl(name, null, null);
    }

    static Type create(String name, String cardinality)
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

