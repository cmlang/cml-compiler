package cml.language.loader;

import cml.language.expressions.TempExpression;
import cml.language.features.Association;
import cml.language.features.AssociationEnd;
import cml.language.features.TempConcept;
import cml.language.foundation.TempModel;
import cml.language.foundation.TempProperty;

@SuppressWarnings("unused")
public interface ModelVisitor
{
    default void visit(TempModel model) {}
    default void visit(TempConcept concept) {}
    default void visit(TempProperty property) {}
    default void visit(Association association) {}
    default void visit(AssociationEnd associationEnd) {}
    default void visit(TempExpression expression) {}
}
