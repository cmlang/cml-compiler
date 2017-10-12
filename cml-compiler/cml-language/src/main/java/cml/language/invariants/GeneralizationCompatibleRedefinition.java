package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cml.language.functions.TypeFunctions.isAssignableFrom;

public class GeneralizationCompatibleRedefinition implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        return getRedefinedProperties(self).allMatch(p -> isAssignableFrom(p.getType(), self.getType()));
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        final List<Property> conflictingProperties = getRedefinedProperties(self)
            .filter(p -> !isAssignableFrom(p.getType(), self.getType()))
            .collect(Collectors.toList());

        return new Diagnostic("generalization_compatible_redefinition", self, conflictingProperties);
    }

    private Stream<Property> getRedefinedProperties(Property self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof TempConcept)
        {
            final TempConcept concept = (TempConcept) self.getParent().get();

            return concept.getInheritedProperties().stream()
                          .filter(p -> p.getName().equals(self.getName()));
        }
        else
        {
            return Stream.empty();
        }
    }
}
