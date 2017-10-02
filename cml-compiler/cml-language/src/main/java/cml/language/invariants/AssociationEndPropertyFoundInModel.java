package cml.language.invariants;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.AssociationEnd;
import cml.language.generated.ModelElement;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public class AssociationEndPropertyFoundInModel implements Invariant<AssociationEnd>
{
    @Override
    public boolean evaluate(AssociationEnd self)
    {
        return self.getAssociatedConcept().isPresent() && self.getAssociatedProperty().isPresent();
    }

    @Override
    public Diagnostic createDiagnostic(AssociationEnd self)
    {
        @SuppressWarnings("ConstantConditions")
        final List<ModelElement> participants = concat(of(self.getAssociatedConcept()), of(self.getAssociatedProperty()))
            .filter(Optional::isPresent)
            .map(e -> (ModelElement)e.get())
            .collect(toList());

        return new Diagnostic("association_end_property_found_in_model", self, participants);
    }
}
