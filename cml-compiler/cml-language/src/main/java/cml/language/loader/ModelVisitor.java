package cml.language.loader;

import cml.language.features.TempConcept;
import cml.language.foundation.TempModel;
import cml.language.foundation.TempProperty;
import cml.language.generated.Association;
import cml.language.generated.AssociationEnd;
import cml.language.generated.Expression;

@SuppressWarnings("unused")
public interface ModelVisitor
{
    default void visit(TempModel model) {}
    default void visit(TempConcept concept) {}
    default void visit(TempProperty property) {}
    default void visit(Association association) {}
    default void visit(AssociationEnd associationEnd) {}
    default void visit(Expression expression) {}
}
