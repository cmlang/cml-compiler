package cml.language.invariants;

import cml.language.features.TempConcept;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Invariant;
import cml.language.generated.Property;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class AbstractPropertyRedefinition implements Invariant<TempConcept>
{
    @Override
    public boolean evaluate(TempConcept self)
    {
        return self.isAbstraction() || getInheritedAbstractProperties(self).allMatch(abstractPropertyRedefinedIn(self));
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
        final List<Property> abstractProperties = getInheritedAbstractProperties(self)
            .filter(abstractPropertyRedefinedIn(self).negate())
            .collect(toList());

        return new Diagnostic("abstract_property_redefinition", self, abstractProperties);
    }

    private Stream<Property> getInheritedAbstractProperties(TempConcept self)
    {
        return self.getAncestors()
                   .stream()
                   .flatMap(c -> c.getAllProperties().stream())
                   .filter(Property::isAbstract);
    }
}