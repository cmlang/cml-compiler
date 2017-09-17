package cml.language.functions;

import cml.language.features.Concept;
import cml.language.features.ConceptRedef;
import cml.language.features.PropertyRedef;
import cml.language.foundation.Property;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

@SuppressWarnings("WeakerAccess")
public class ConceptFunctions
{
    public static List<Property> redefinedInheritedConcreteProperties(Concept concept)
    {
        return concept.getInheritedProperties()
                      .stream()
                      .filter(Property::isConcrete)
                      .map(p -> propertyOf(concept, p.getName()).orElse(p))
                      .collect(toList());
    }

    public static Optional<Property> propertyOf(Concept concept, String propertyName)
    {
        return concept.getProperties().stream()
                      .filter(p -> p.getName().equals(propertyName))
                      .findFirst();
    }

    public static Function<Concept, ConceptRedef> conceptRedefined(Concept concept, Concept redefBase)
    {
        return c -> {
            final List<PropertyRedef> propertyRedefs = c.getNonDerivedProperties()
                                                        .stream()
                                                        .map(p -> propertyOf(concept, p.getName()).orElse(p))
                                                        .map(p -> new PropertyRedef(p, p.getConcept() == redefBase))
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

    public static List<ConceptRedef> redefinedAncestors(Concept concept)
    {
        return redefinedAncestors(concept, concept);
    }

    public static List<ConceptRedef> redefinedAncestors(Concept concept, Concept redefBase)
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
