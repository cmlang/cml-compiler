package cml.language.functions;

import cml.language.features.ConceptRedef;
import cml.language.features.PropertyRedef;
import cml.language.features.TempConcept;
import cml.language.foundation.TempProperty;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@SuppressWarnings("WeakerAccess")
public class ConceptFunctions
{
    public static List<TempProperty> redefinedInheritedConcreteProperties(TempConcept concept)
    {
        return concept.getInheritedProperties()
                      .stream()
                      .filter(TempProperty::isConcrete)
                      .map(p -> propertyOf(concept, p.getName()).orElse(p))
                      .collect(toList());
    }

    public static Optional<TempProperty> propertyOf(TempConcept concept, String propertyName)
    {
        return concept.getProperties().stream()
                      .map(p -> (TempProperty)p)
                      .filter(p -> p.getName().equals(propertyName))
                      .findFirst();
    }

    public static Function<TempConcept, ConceptRedef> conceptRedefined(TempConcept concept, TempConcept redefBase)
    {
        return c -> {
            final List<PropertyRedef> propertyRedefs = c.getNonDerivedProperties()
                                                        .stream()
                                                        .map(p -> propertyOf(concept, p.getName()).orElse(p))
                                                        .filter(p -> p.getConcept().isPresent())
                                                        .map(p -> new PropertyRedef(p, p.getConcept().get() == redefBase))
                                                        .collect(toList());

            return new ConceptRedef(c, propertyRedefs);
        };
    }

    public static Function<ConceptRedef, ConceptRedef> overridden(List<ConceptRedef> redefinitions)
    {
        return conceptRedef -> {
            final Optional<ConceptRedef> override = redefinitions.stream()
                                                                 .filter(o -> o.getConcept() == conceptRedef.getConcept())
                                                                 .findFirst();

            if (override.isPresent())
            {
                final List<PropertyRedef> propertyRedefs = conceptRedef.getPropertyRedefs()
                                                                       .stream()
                                                                       .map(p -> override.get().getPropertyRedef(p).orElse(p))
                                                                       .collect(toList());

                return new ConceptRedef(conceptRedef.getConcept(), propertyRedefs);
            }
            else
            {
                return conceptRedef;
            }
        };
    }

    public static List<ConceptRedef> redefinedAncestors(TempConcept concept)
    {
        return redefinedAncestors(concept, concept);
    }

    public static List<ConceptRedef> redefinedAncestors(TempConcept concept, TempConcept redefBase)
    {
        final List<ConceptRedef> redefinitions = concept.getAllAncestors()
                                                        .stream()
                                                        .map(conceptRedefined(concept, redefBase))
                                                        .collect(toList());

        final Stream<ConceptRedef> inheritedRedefinitions = concept.getDirectAncestors()
                                                                   .stream()
                                                                   .flatMap(c -> redefinedAncestors(c, redefBase)
                                                                   .stream())
                                                                   .map(overridden(redefinitions));

        return concat(inheritedRedefinitions, redefinitions.stream())
            .distinct()
            .collect(toList());
    }

}
