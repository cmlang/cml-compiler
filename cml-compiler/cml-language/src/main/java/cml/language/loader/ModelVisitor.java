package cml.language.loader;

import cml.language.features.Association;
import cml.language.features.TempAssociationEnd;
import cml.language.features.TempConcept;
import cml.language.foundation.TempModel;
import cml.language.foundation.TempProperty;
import cml.language.generated.Expression;

@SuppressWarnings("unused")
public interface ModelVisitor
{
    default void visit(TempModel model) {}
    default void visit(TempConcept concept) {}
    default void visit(TempProperty property) {}
    default void visit(Association association) {}
    default void visit(TempAssociationEnd associationEnd) {}
    default void visit(Expression expression) {}
}
