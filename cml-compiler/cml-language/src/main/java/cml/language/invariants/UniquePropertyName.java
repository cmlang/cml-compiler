package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UniquePropertyName implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        return getConflictingProperties(self).count() == 0;
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        final List<Property> participants = getConflictingProperties(self).collect(Collectors.toList());

        return new Diagnostic("unique_property_name", self, participants);
    }

    private Stream<Property> getConflictingProperties(Property self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof TempConcept)
        {
            final TempConcept concept = (TempConcept) self.getParent().get();

            return concept.getProperties()
                          .stream()
                          .filter(p -> p != self && p.getName().equals(self.getName()));
        }
        else
        {
            return Stream.empty();
        }
    }
}
