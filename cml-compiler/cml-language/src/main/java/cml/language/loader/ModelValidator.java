package cml.language.loader;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.InvariantValidator;
import cml.language.foundation.TempProperty;
import cml.language.generated.Association;
import cml.language.generated.AssociationEnd;
import cml.language.generated.Expression;
import cml.language.invariants.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ModelValidator implements ModelVisitor
{
    private final InvariantValidator<TempConcept> conceptInvariantValidator = TempConcept.invariantValidator();
    private final InvariantValidator<TempProperty> propertyInvariantValidator = TempProperty.invariantValidator();
    private final InvariantValidator<Association> associationInvariantValidator = () -> asList(
        new AssociationMustHaveTwoAssociationEnds(),
        new AssociationEndTypesMustMatch()
    );
    private final InvariantValidator<AssociationEnd> associationEndInvariantValidator = () -> asList(
        new AssociationEndPropertyFoundInModel(),
        new AssociationEndTypeMatchesPropertyType()
    );
    private final InvariantValidator<Expression> expressionInvariantValidator = () -> singletonList(new ExpressionInvariant());
    private final List<Diagnostic> diagnostics = new ArrayList<>();

    List<Diagnostic> getDiagnostics()
    {
        return diagnostics;
    }

    @Override
    public void visit(TempConcept concept)
    {
        conceptInvariantValidator.validate(concept, diagnostics);
    }

    @Override
    public void visit(TempProperty property)
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
