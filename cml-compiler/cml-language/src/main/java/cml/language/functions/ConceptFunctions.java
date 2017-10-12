package cml.language.functions;

import cml.language.features.TempConcept;
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
                                                        .map(conceptRedefined(concept, redefBase))
                                                        .collect(toList());

        final Stream<ConceptRedef> inheritedRedefinitions = concept.getDirectAncestors()
                                                                   .stream()
                                                                   .flatMap(c -> redefinedAncestors(c, redefBase)
                                                                   .stream())
                                                                   .map(overridden(redefinitions));

        return seq(concat(inheritedRedefinitions, redefinitions.stream()))
            .distinct(r -> r.getConcept())
            .collect(toList());
    }

}
