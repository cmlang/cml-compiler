package cml.language.loader;

import cml.language.expressions.Expression;
import cml.language.features.Association;
import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.foundation.Model;
import cml.language.foundation.Property;

@SuppressWarnings("unused")
public interface ModelVisitor
{
    default void visit(Model model) {}
    default void visit(Concept concept) {}
    default void visit(Property property) {}
    default void visit(Association association) {}
    default void visit(AssociationEnd associationEnd) {}
    default void visit(Expression expression) {}
}
