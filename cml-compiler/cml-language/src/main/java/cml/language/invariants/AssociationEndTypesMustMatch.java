package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.foundation.TempProperty;
import cml.language.generated.Association;
import cml.language.generated.AssociationEnd;

import java.util.Optional;

import static cml.language.functions.ModelElementFunctions.selfTypeOf;
import static cml.language.functions.TypeFunctions.isEqualTo;

public class AssociationEndTypesMustMatch implements Invariant<Association>
{
    @Override
    public boolean evaluate(Association self)
    {
        if (self.getAssociationEnds().size() != 2)
        {
            return true;
        }

        final Optional<AssociationEnd> first = self.getAssociationEnds().stream().findFirst();
        final Optional<AssociationEnd> last = self.getAssociationEnds().stream().reduce((previous, next) -> next);

        if (!first.isPresent() || !last.isPresent())
        {
            return true;
        }

        final AssociationEnd end1 = first.get();
        final AssociationEnd end2 = last.get();

        if (!end1.getAssociatedConcept().isPresent() || !end1.getAssociatedProperty().isPresent() ||
            !end2.getAssociatedConcept().isPresent() || !end2.getAssociatedProperty().isPresent())
        {
            return true;
        }

        final TempConcept firstConcept = end1.getAssociatedConcept().map(c -> (TempConcept) c).get();
        final TempConcept secondConcept = end2.getAssociatedConcept().map(c -> (TempConcept) c).get();
        final TempProperty firstProperty = end1.getAssociatedProperty().map(p -> (TempProperty) p).get();
        final TempProperty secondProperty = end2.getAssociatedProperty().map(p -> (TempProperty) p).get();

        return typesMatch(firstConcept, secondProperty) && typesMatch(secondConcept, firstProperty);
    }

    private static boolean typesMatch(TempConcept concept, TempProperty property)
    {
        return isEqualTo(property.getType().getElementType(), selfTypeOf(concept));
    }

    @Override
    public Diagnostic createDiagnostic(Association self)
    {
        return new Diagnostic("association_end_types_must_match", self, self.getAssociationEnds());
    }
}
