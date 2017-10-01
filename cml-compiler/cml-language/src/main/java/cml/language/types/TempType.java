package cml.language.types;

import cml.language.features.TempConcept;
import cml.language.generated.ModelElement;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static java.util.Optional.empty;

public interface TempType extends ModelElement
{
    String REQUIRED = "required";
    String OPTIONAL = "optional";
    String SEQUENCE = "sequence";

    default Optional<String> getCardinality()
    {
        return empty();
    }

    default Optional<String> getErrorMessage()
    {
        return empty();
    }

    TempType getElementType();

    default TempType getMatchingResultType()
    {
        return this;
    }

    default TempType getBaseType()
    {
        return this;
    }

    default boolean isParameter() { return isDefined() && !isPrimitive() && !isConcept(); }

    default boolean isConcept() { return getConcept().isPresent(); }

    default boolean isPrimitive()
    {
        return false;
    }

    default boolean isBoolean()
    {
        return false;
    }

    default boolean isString()
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

    default boolean isRelational()
    {
        return isString() || isNumeric() || isBinaryFloatingPoint();
    }

    default boolean isReferential()
    {
        return isConcept() && !isSequence();
    }

    default boolean isDefined()
    {
        return !isUndefined();
    }

    default boolean isUndefined()
    {
        return false;
    }

    default boolean isSingle()
    {
        return (isRequired() || isOptional()) && !isSequence();
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

    default String getKind()
    {
        if (getCardinality().isPresent())
        {
            final String cardinality = getCardinality().get();

            if (cardinality.equals("?"))
            {
                return OPTIONAL;
            }
            else if (cardinality.equals("*"))
            {
                return SEQUENCE;
            }
        }

        return REQUIRED;
    }

    default Optional<TempConcept> getConcept()
    {
        return empty();
    }

    default void setConcept(@NotNull TempConcept concept)
    {
        throw new UnsupportedOperationException("setConcept");
    }
}
