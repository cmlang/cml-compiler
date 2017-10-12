package cml.language.loader;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.InvariantValidator;
import cml.language.generated.Association;
import cml.language.generated.AssociationEnd;
import cml.language.generated.Expression;
import cml.language.generated.Property;
import cml.language.invariants.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ModelValidator implements ModelVisitor
{
    private final InvariantValidator<TempConcept> conceptInvariantValidator = () -> asList(
        new NotOwnGeneralization(),
        new CompatibleGeneralizations(),
        new ConflictRedefinition(),
        new AbstractPropertyRedefinition()
    );

    private final InvariantValidator<Property> propertyInvariantValidator = () -> asList(
        new UniquePropertyName(),
        new PropertyTypeSpecifiedOrInferred(),
        new PropertyTypeAssignableFromExpressionType(),
        new GeneralizationCompatibleRedefinition(),
        new AbstractPropertyInAbstractConcept()
    );

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
