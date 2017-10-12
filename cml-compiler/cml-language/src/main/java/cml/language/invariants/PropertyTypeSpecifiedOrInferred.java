package cml.language.invariants;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;

public class PropertyTypeSpecifiedOrInferred implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        return self.getType().isDefined();
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        return new Diagnostic(
            "property_type_specified_or_inferred",
            self,
            self.getType().getErrorMessage().orElse(null));
    }

}
