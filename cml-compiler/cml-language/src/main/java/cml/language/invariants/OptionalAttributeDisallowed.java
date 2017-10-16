package cml.language.invariants;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;
import cml.language.generated.Type;

public class OptionalAttributeDisallowed implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        final Type type = self.getType();

        return !type.isPrimitive() || !type.isOptional();
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        return new Diagnostic("optional_attribute_disallowed", self);
    }

}
