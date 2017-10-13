package cml.language.functions;

import cml.language.features.TempConcept;
import cml.language.foundation.Pair;
import cml.language.generated.ConceptRedef;
import cml.language.generated.Property;
import cml.language.generated.PropertyRedef;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static cml.language.generated.ConceptRedef.createConceptRedef;
import static cml.language.generated.PropertyRedef.createPropertyRedef;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.jooq.lambda.Seq.seq;

@SuppressWarnings("WeakerAccess")
public class ConceptFunctions
{
    public static List<Property> redefinedInheritedConcreteProperties(TempConcept concept)
    {
        return concept.getInheritedProperties()
                      .stream()
                      .filter(Property::isConcrete)
                      .map(p -> propertyOf(concept, p.getName()).orElse(p))
                      .collect(toList());
    }

    public static Optional<Property> propertyOf(TempConcept concept, String propertyName)
    {
        return concept.getProperties().stream()
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
                                                        .map(p -> createPropertyRedef(p, p.getConcept().get() == redefBase))
                                                        .collect(toList());

            return createConceptRedef(c, propertyRedefs);
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
                                                                       .map(p -> propertyRedefOf(override.get().getPropertyRedefs(), p).orElse(p))
                                                                       .collect(toList());

                return createConceptRedef(conceptRedef.getConcept(), propertyRedefs);
            }
            else
            {
                return conceptRedef;
            }
        };
    }

    public static Optional<PropertyRedef> propertyRedefOf(List<PropertyRedef> propertyRedefs, PropertyRedef propertyRedef)
    {
        return propertyRedefs.stream()
                             .filter(p -> p.getProp().getName().equals(propertyRedef.getProp().getName()))
                             .filter(p -> p.isRedefined())
                             .findFirst();
    }

    public static List<ConceptRedef> redefinedAncestors(TempConcept concept)
    {
        return redefinedAncestors(concept, concept);
    }

    public static List<ConceptRedef> redefinedAncestors(TempConcept concept, TempConcept redefBase)
    {
        final List<ConceptRedef> redefinitions = concept.getAllAncestors()
                                                        .stream()
                                                        .map(c -> (TempConcept)c)
                                                        .map(conceptRedefined(concept, redefBase))
                                                        .collect(toList());

        final Stream<ConceptRedef> inheritedRedefinitions = concept.getAncestors()
                                                                   .stream()
                                                                   .flatMap(c -> redefinedAncestors((TempConcept) c, redefBase)
                                                                   .stream())
                                                                   .map(overridden(redefinitions));

        return seq(concat(inheritedRedefinitions, redefinitions.stream()))
            .distinct(r -> r.getConcept())
            .collect(toList());
    }

    public static List<Pair<TempConcept>> generalizationPairs(TempConcept concept)
    {
        return concept.getAncestors().stream().flatMap(
            c1 -> concept.getAncestors().stream()
                                .filter(c2 -> c1 != c2)
                                .map(c2 -> new Pair<>((TempConcept)c1, (TempConcept)c2))
        )
                             .distinct()
                             .collect(toList());
    }

    public static List<Pair<Property>> generalizationPropertyPairs(TempConcept concept)
    {
        return generalizationPairs(concept).stream().flatMap(pair ->
            pair.getLeft().getAllProperties().stream().flatMap(p1 ->
                pair.getRight()
                    .getAllProperties()
                    .stream()
                    .filter(p2 -> p1 != p2)
                    .filter(p2 -> p1.getName().equals(p2.getName()))
                    .map(p2 -> new Pair<>(p1, p2))
            )
        )
        .distinct()
        .collect(toList());
    }
}
