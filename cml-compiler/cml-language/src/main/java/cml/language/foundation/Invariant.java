package cml.language.foundation;

import cml.language.generated.ModelElement;

public interface Invariant<T extends ModelElement>
{
    boolean evaluate(T self);
    Diagnostic createDiagnostic(T self);

    default boolean isCritical()
    {
        return false;
    }
}
