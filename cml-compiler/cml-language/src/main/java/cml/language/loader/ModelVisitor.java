package cml.language.loader;

import cml.language.features.TempConcept;
import cml.language.foundation.TempModel;
import cml.language.generated.Association;
import cml.language.generated.AssociationEnd;
import cml.language.generated.Expression;
import cml.language.generated.Property;

@SuppressWarnings("unused")
public interface ModelVisitor
{
    default void visit(TempModel model) {}
    default void visit(TempConcept concept) {}
    default void visit(Property property) {}
    default void visit(Association association) {}
    default void visit(AssociationEnd associationEnd) {}
    default void visit(Expression expression) {}
}
