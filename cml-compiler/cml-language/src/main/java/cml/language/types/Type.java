package cml.language.types;

import cml.language.features.Concept;
import cml.language.foundation.ModelElement;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public interface Type extends ModelElement
{
    String getKind();
    Optional<String> getCardinality();

    Optional<String> getErrorMessage();

    default Type getElementType()
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
                        .anyMatch(c -> c.getSelfType().equals(getElementType()));
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

    Optional<Concept> getConcept();
    void setConcept(@Nullable Concept concept);
}
