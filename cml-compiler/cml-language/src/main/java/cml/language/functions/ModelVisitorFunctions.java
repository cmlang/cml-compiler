package cml.language.functions;

import cml.language.features.Function;
import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.foundation.TempModel;
import cml.language.generated.Association;
import cml.language.generated.Expression;
import cml.language.generated.Property;
import cml.language.loader.ModelVisitor;

import static org.jooq.lambda.Seq.seq;

@SuppressWarnings("WeakerAccess")
public class ModelVisitorFunctions
{
    public static void visitModel(TempModel model, ModelVisitor visitor)
    {
        visitor.visit(model);

        model.getModules().forEach(m -> visitModule((TempModule) m, visitor));

        model.getConcepts().forEach(c -> visitConcept((TempConcept) c, visitor));

        model.getAssociations().forEach(a -> visitAssociation(a, visitor));
    }

    public static void visitModule(TempModule module, ModelVisitor visitor)
    {
        visitor.visit(module);

        module.getDefinedFunctions().forEach(f -> visitFunction(f, visitor));
    }

    public static void visitFunction(Function function, ModelVisitor visitor)
    {
        visitor.visit(function);

        function.getExpression().ifPresent(expression -> visitExpression(expression, visitor));
    }

    public static void visitConcept(TempConcept concept, ModelVisitor visitor)
    {
        visitor.visit(concept);

        seq(concept.getProperties()).forEach(p -> visitProperty(p, visitor));
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

        expression.getOperands().forEach(e -> visitExpression(e, visitor));
    }
}
