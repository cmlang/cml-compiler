package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;

import java.util.List;
import java.util.stream.Stream;

import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static java.util.stream.Collectors.toList;

public class CompatibleGeneralizations implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return self.getGeneralizationPropertyPairs()
                   .stream()
                   .allMatch(pair -> isAssignableFrom(pair.getLeft().getType(), pair.getRight().getType()));
    }

    @Override
    public Diagnostic createDiagnostic(TempConcept self)
    {
        final List<Property> conflictingProperties = self.getGeneralizationPropertyPairs().stream()
                                                         .filter(pair -> !isAssignableFrom(pair.getLeft().getType(), pair.getRight().getType()))
                                                         .flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()))
                                                         .collect(toList());

        return new Diagnostic("compatible_generalizations", self, conflictingProperties);
    }
}
