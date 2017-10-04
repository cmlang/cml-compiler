package cml.language.functions;

import cml.language.features.TempConcept;
import cml.language.foundation.TempModel;
import cml.language.foundation.TempProperty;
import cml.language.generated.Association;
import cml.language.generated.Expression;
import cml.language.loader.ModelVisitor;

import static org.jooq.lambda.Seq.seq;

@SuppressWarnings("WeakerAccess")
public class ModelVisitorFunctions
{
    public static void visitModel(TempModel model, ModelVisitor visitor)
    {
        visitor.visit(model);

        model.getConcepts().forEach(c -> visitConcept(c, visitor));

        model.getAssociations().forEach(a -> visitAssociation(a, visitor));
    }

    public static void visitConcept(TempConcept concept, ModelVisitor visitor)
    {
        visitor.visit(concept);

        seq(concept.getProperties()).map(p -> (TempProperty)p).forEach(p -> visitProperty(p, visitor));
    }

    public static void visitAssociation(Association association, ModelVisitor visitor)
    {
        visitor.visit(association);

        association.getAssociationEnds().forEach(visitor::visit);
    }

    public static void visitProperty(TempProperty property, ModelVisitor visitor)
    {
        visitor.visit(property);

        property.getValue().ifPresent(expression -> visitExpression(expression, visitor));
    }

    public static void visitExpression(Expression expression, ModelVisitor visitor)
    {
        visitor.visit(expression);

        ((Expression)expression).getSubExpressions().forEach(e -> visitExpression(e, visitor));
    }
}
