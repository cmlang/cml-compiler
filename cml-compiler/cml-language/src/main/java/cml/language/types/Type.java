package cml.language.types;

import cml.language.features.Concept;
import cml.language.foundation.ModelElement;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

import static cml.language.functions.ModelElementFunctions.selfTypeOf;
import static java.util.Optional.empty;

public interface Type extends ModelElement
{
    String REQUIRED = "required";
    String OPTIONAL = "optional";
    String SEQUENCE = "sequence";

    default String getKind()
    {
        return REQUIRED;
    }

    default Optional<String> getCardinality()
    {
        return empty();
    }

    default Optional<String> getErrorMessage()
    {
        return empty();
    }

    default Type getElementType()
    {
        return this;
    }

    default Type getMatchingResultType()
    {
        return this;
    }

    default Type getBaseType()
    {
        return this;
    }

    default Type withCardinality(String cardinality)
    {
        throw new UnsupportedOperationException(this + "withCardinality(" + cardinality + ")");
    }

    default boolean isParameter() { return isDefined() && !isPrimitive() && !isConcept(); }

    default boolean isConcept() { return getConcept().isPresent(); }

    default boolean isPrimitive()
    {
        return false;
    }

    default boolean isNumeric()
    {
        return false;
    }

    default boolean isBinaryFloatingPoint()
    {
        return false;
    }

    default boolean isDefined()
    {
        return !isUndefined();
    }

    default boolean isUndefined()
    {
        return false;
    }

    default boolean isOptional()
    {
        return false;
    }

    default boolean isRequired()
    {
        return false;
    }

    default boolean isSingle()
    {
        return (isRequired() || isOptional()) && !isSequence();
    }

    default boolean isSequence()
    {
        return false;
    }

    default boolean isNumericWiderThan(Type other)
    {
        return false;
    }

    default boolean isBinaryFloatingPointWiderThan(Type other)
    {
        return false;
    }

    default boolean isAssignableFrom(Type other)
    {
        return this.isTypeAssignableFrom(other) && this.isCardinalityAssignableFrom(other);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    default boolean isTypeAssignableFrom(Type other)
    {
        if (getElementType().equals(other.getElementType()))
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
                        .anyMatch(c -> selfTypeOf(c).equals(getElementType()));
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

    default Optional<Concept> getConcept()
    {
        return empty();
    }

    default void setConcept(@NotNull Concept concept)
    {
        throw new UnsupportedOperationException("setConcept");
    }
}
