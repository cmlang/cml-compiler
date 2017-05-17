package cml.language;

import cml.language.features.Concept;
import cml.language.foundation.Property;

public class ModelVisitor
{
    public interface Delegate
    {
        void visit(Model model);
        void visit(Concept concept);
        void visit(Property property);
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

            for(final Property property: concept.getProperties())
            {
                delegate.visit(property);
            }
        }
    }
}
