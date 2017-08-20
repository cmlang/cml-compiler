package cml.language.foundation;

import java.util.List;

@FunctionalInterface
public interface InvariantValidator<T extends ModelElement>
{
    List<Invariant<T>> getInvariants();

    default void validate(T self, List<Diagnostic> diagnostics)
    {
        for (Invariant<T> invariant: getInvariants())
        {
            if (!invariant.evaluate(self))
            {
                diagnostics.add(invariant.createDiagnostic(self));

                if (invariant.isCritical()) break;
            }
        }
    }
}
