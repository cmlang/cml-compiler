package cml.language.functions;

import cml.language.expressions.Expression;
import cml.language.features.Association;
import cml.language.features.Concept;
import cml.language.foundation.Model;
import cml.language.foundation.Property;
import cml.language.loader.ModelVisitor;

@SuppressWarnings("WeakerAccess")
public class ModelVisitorFunctions
{
    public static void visitModel(Model model, ModelVisitor visitor)
    {
        visitor.visit(model);

        model.getConcepts().forEach(c -> visitConcept(c, visitor));

        model.getAssociations().forEach(a -> visitAssociation(a, visitor));
    }

    public static void visitConcept(Concept concept, ModelVisitor visitor)
    {
        visitor.visit(concept);

        concept.getProperties().forEach(p -> visitProperty(p, visitor));
    }

    public static void visitAssociation(Association association, ModelVisitor visitor)
    {
        visitor.visit(association);

        association.getAssociationEnds().forEach(visitor::visit);
    }

    public static void visitProperty(Property property, ModelVisitor visitor)
    {
        visitor.visit(property);

        property.getValue().ifPresent(expression -> visitExpression(expression, visitor));
    }

    public static void visitExpression(Expression expression, ModelVisitor visitor)
    {
        visitor.visit(expression);

        expression.getSubExpressions().forEach(e -> visitExpression(e, visitor));
    }
}
