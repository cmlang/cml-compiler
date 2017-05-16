package cml.language;

import cml.language.features.Concept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.InvariantValidator;

import java.util.ArrayList;
import java.util.List;

public class ModelValidator implements ModelVisitor.Delegate
{
    private final InvariantValidator<Concept> conceptInvariantValidator = Concept.invariantValidator();
    private final List<Diagnostic> diagnostics = new ArrayList<>();

    public List<Diagnostic> getDiagnostics()
    {
        return diagnostics;
    }

    @Override
    public void visit(Model model) {}

    @Override
    public void visit(Concept concept)
    {
        conceptInvariantValidator.validate(concept, diagnostics);
    }
}
