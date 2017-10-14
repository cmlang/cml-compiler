package cml.language.invariants;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;
import cml.language.generated.UndefinedType;

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
        assert self.getType() instanceof UndefinedType;

        final UndefinedType undefinedType = (UndefinedType) self.getType();

        return new Diagnostic(
            "property_type_specified_or_inferred",
            self,
            undefinedType.getErrorMessage());
    }

}
