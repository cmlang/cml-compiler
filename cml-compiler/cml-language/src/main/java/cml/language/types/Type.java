package cml.language.types;

import cml.language.features.Concept;
import cml.language.generated.ModelElement;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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

    Type getElementType();

    default Type getMatchingResultType()
    {
        return this;
    }

    default Type getBaseType()
    {
        return this;
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

    default Optional<Concept> getConcept()
    {
        return empty();
    }

    default void setConcept(@NotNull Concept concept)
    {
        throw new UnsupportedOperationException("setConcept");
    }
}
