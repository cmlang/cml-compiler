package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;

public class NotOwnGeneralization implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return !self.getAllGeneralizations().contains(self);
    }

    @Override
    public Diagnostic createDiagnostic(TempConcept self)
    {
        return new Diagnostic("not_own_generalization", self);
    }

    @Override
    public boolean isCritical()
    {
        return true;
    }
}
