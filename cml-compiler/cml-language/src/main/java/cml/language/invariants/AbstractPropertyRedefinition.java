package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;

public class AbstractPropertyRedefinition implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return self.isAbstraction() || seq(self.getInheritedAbstractProperties()).allMatch(abstractPropertyRedefinedIn(self));
    }

    private Predicate<Property> abstractPropertyRedefinedIn(TempConcept self)
    {
        return p1 -> self.getProperties()
                         .stream()
                         .filter(p2 -> p1.getName().equals(p2.getName()))
                         .filter(Property::isConcrete)
                         .count() > 0;
    }

    @Override
    public Diagnostic createDiagnostic(TempConcept self)
    {
        final List<Property> abstractProperties = seq(self.getInheritedAbstractProperties())
            .filter(abstractPropertyRedefinedIn(self).negate())
            .collect(toList());

        return new Diagnostic("abstract_property_redefinition", self, abstractProperties);
    }
}