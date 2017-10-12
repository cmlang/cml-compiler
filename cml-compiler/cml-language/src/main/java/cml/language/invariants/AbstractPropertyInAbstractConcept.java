package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;

import static java.util.Collections.emptyList;

public class AbstractPropertyInAbstractConcept implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof TempConcept)
        {
            final TempConcept concept = (TempConcept) self.getParent().get();

            return self.isConcrete() || concept.isAbstraction();
        }
        else
        {
            return self.isConcrete();
        }
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        return new Diagnostic("abstract_property_in_abstract_concept", self, emptyList());
    }
}
