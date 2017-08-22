package cml.language.loader;

import cml.language.expressions.Expression;
import cml.language.features.Association;
import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.InvariantValidator;
import cml.language.foundation.Property;

import java.util.ArrayList;
import java.util.List;

public class ModelValidator implements ModelVisitor
{
    private final InvariantValidator<Concept> conceptInvariantValidator = Concept.invariantValidator();
    private final InvariantValidator<Property> propertyInvariantValidator = Property.invariantValidator();
    private final InvariantValidator<Association> associationInvariantValidator = Association.invariantValidator();
    private final InvariantValidator<AssociationEnd> associationEndInvariantValidator = AssociationEnd.invariantValidator();
    private final InvariantValidator<Expression> expressionInvariantValidator = Expression.invariantValidator();
    private final List<Diagnostic> diagnostics = new ArrayList<>();

    List<Diagnostic> getDiagnostics()
    {
        return diagnostics;
    }

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
    public void visit(Association association)
    {
        associationInvariantValidator.validate(association, diagnostics);
    }

    @Override
    public void visit(AssociationEnd associationEnd)
    {
        associationEndInvariantValidator.validate(associationEnd, diagnostics);
    }

    @Override
    public void visit(final Expression expression)
    {
        expressionInvariantValidator.validate(expression, diagnostics);
    }
}
