package cml.language.foundation;

import cml.language.generated.PropertyList;
import cml.language.generated.Scope;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public interface TempPropertyList extends PropertyList
{
    default List<TempProperty> getDerivedProperties()
    {
        return getProperties().stream()
                              .filter(TempProperty::isDerived)
                              .collect(toList());
    }

    default List<TempProperty> getNonDerivedProperties()
    {
        return getProperties().stream()
                              .filter(p -> !p.isDerived())
                              .sorted(byInitOrder())
                              .collect(toList());
    }

    static Comparator<TempProperty> byInitOrder()
    {
        return (TempProperty p1, TempProperty p2) -> {
            if (p1.getValue().isPresent() && !p2.getValue().isPresent()) return +1;
            else if (!p1.getValue().isPresent() && p2.getValue().isPresent()) return -1;
            else return 0;
        };
    }

    default List<TempProperty> getProperties()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof TempProperty)
                           .map(e -> (TempProperty)e)
                           .collect(toList());
    }
}

