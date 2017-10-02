package cml.language.invariants;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.AssociationEnd;

import static cml.language.functions.TypeFunctions.isEqualTo;
import static java.util.Collections.singletonList;

public class AssociationEndTypeMatchesPropertyType implements Invariant<AssociationEnd>
{
    @Override
    public boolean evaluate(AssociationEnd self)
    {
        return !self.getPropertyType().isPresent() ||
            !self.getAssociatedProperty().isPresent() ||
            isEqualTo(self.getPropertyType().get(), self.getAssociatedProperty().get().getType());
    }

    @Override
    public Diagnostic createDiagnostic(AssociationEnd self)
    {
        assert self.getAssociatedProperty().isPresent();

        return new Diagnostic(
            "association_end_type_matches_property_type",
            self,
            singletonList(self.getAssociatedProperty().get()));
    }
}