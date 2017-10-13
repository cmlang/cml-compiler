package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.foundation.Pair;
import cml.language.generated.Property;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static cml.language.functions.ConceptFunctions.generalizationPropertyPairs;
import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static java.util.stream.Collectors.toList;

public class ConflictRedefinition implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return getConflictingPropertyPairs(self)
            .map(Pair::getLeft)
            .allMatch(propertyRedefinedIn(self));
    }

    private Stream<Pair<Property>> getConflictingPropertyPairs(TempConcept self)
    {
        return generalizationPropertyPairs(self)
                   .stream()
                   .filter(pair -> isAssignableFrom(pair.getLeft().getType(), pair.getRight().getType()))
                   .filter(pair -> pair.getLeft().isDerived() ||
                       pair.getLeft().getValue().isPresent() ||
                       pair.getRight().isDerived() ||
                       pair.getRight().getValue().isPresent());
    }

    private Predicate<Property> propertyRedefinedIn(TempConcept self)
    {
        return p1 -> self.getProperties()
                         .stream()
                         .anyMatch(p2 -> p1.getName().equals(p2.getName()));
    }

    @Override
    public Diagnostic createDiagnostic(TempConcept self)
    {
        final List<Property> conflictingProperties = getConflictingPropertyPairs(self)
            .flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()))
            .filter(propertyRedefinedIn(self).negate())
            .collect(toList());

        return new Diagnostic("conflict_redefinition", self, conflictingProperties);
    }
}