package cml.language;

import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.InvariantValidator;
import cml.language.foundation.Property;

import java.util.ArrayList;
import java.util.List;

public class ModelValidator implements ModelVisitor.Delegate
{
    private final InvariantValidator<Concept> conceptInvariantValidator = Concept.invariantValidator();
    private final InvariantValidator<Property> propertyInvariantValidator = Property.invariantValidator();
    private final InvariantValidator<AssociationEnd> associationEndInvariantValidator = AssociationEnd.invariantValidator();
    private final List<Diagnostic> diagnostics = new ArrayList<>();

    List<Diagnostic> getDiagnostics()
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

    @Override
    public void visit(Property property)
    {
        propertyInvariantValidator.validate(property, diagnostics);
    }

    @Override
    public void visit(AssociationEnd associationEnd)
    {
        associationEndInvariantValidator.validate(associationEnd, diagnostics);
    }
}
