package cml.language.foundation;

import cml.language.features.Concept;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface PropertyList extends Scope
{
    default List<Property> getDerivedProperties()
    {
        return getProperties().stream()
                              .filter(Property::isDerived)
                              .collect(toList());
    }

    default List<Property> getNonDerivedProperties()
    {
        return getProperties().stream()
                              .filter(p -> !p.isDerived())
                              .sorted(byInitOrder())
                              .collect(toList());
    }

    static Comparator<Property> byInitOrder()
    {
        return (Property p1, Property p2) -> {
            if (p1.getValue().isPresent() && !p2.getValue().isPresent()) return +1;
            else if (!p1.getValue().isPresent() && p2.getValue().isPresent()) return -1;
            else return 0;
        };
    }


    default List<Property> getProperties()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Property)
                           .map(e -> (Property)e)
                           .collect(toList());
    }

    default Optional<Property> getProperty(String propertyName)
    {
        return getProperties().stream()
                              .filter(p -> p.getName().equals(propertyName))
                              .findFirst();
    }
}

