package cml.language.invariants;

import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;

import static cml.language.functions.ModelElementFunctions.diagnosticIdentificationOf;
import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static java.lang.String.format;

public class PropertyTypeAssignableFromExpressionType implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        return !(self.getDeclaredType().isPresent() && self.getValue().isPresent()) ||
            isAssignableFrom(self.getDeclaredType().get(), self.getValue().get().getType());
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        assert self.getDeclaredType().isPresent();
        assert self.getValue().isPresent();

        return new Diagnostic(
            "property_type_assignable_from_expression_type",
            self,
            format(
                "Declared type is %s but type inferred from expression is %s.",
                diagnosticIdentificationOf(self.getDeclaredType().get()),
                diagnosticIdentificationOf(self.getValue().get().getType())));
    }

}
