package cml.language;

import cml.language.expressions.Expression;
import cml.language.features.Association;
import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.foundation.Property;

public class ModelVisitor
{
    public interface Delegate
    {
        default void visit(Model model) {}
        default void visit(Concept concept) {}
        default void visit(Property property) {}
        default void visit(Association association) {}
        default void visit(AssociationEnd associationEnd) {}
        default void visit(Expression expression) {}
    }

    private Delegate delegate;

    public ModelVisitor(Delegate delegate)
    {
        this.delegate = delegate;
    }

    public void visit(Model model)
    {
        delegate.visit(model);

        for (final Concept concept: model.getConcepts())
        {
            delegate.visit(concept);

            for (final Property property: concept.getProperties())
            {
                if (property.getValue().isPresent())
                {
                    delegate.visit(property.getValue().get());
                }

                delegate.visit(property);
            }
        }

        for (final Association association: model.getAssociations())
        {
            delegate.visit(association);

            for (final AssociationEnd associationEnd: association.getAssociationEnds())
            {
                delegate.visit(associationEnd);
            }
        }
    }
}
