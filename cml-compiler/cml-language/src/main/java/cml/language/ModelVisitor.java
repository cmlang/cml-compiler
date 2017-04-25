package cml.language;

import cml.language.features.Concept;
import cml.language.foundation.Model;

public class ModelVisitor
{
    public interface Delegate
    {
        void visit(Model model);
        void visit(Concept concept);
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
        }
    }
}
