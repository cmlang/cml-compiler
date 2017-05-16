package cml.language.foundation;

public interface Invariant<T extends ModelElement>
{
    boolean evaluate(T self);
    Diagnostic createDiagnostic(T self);
}
