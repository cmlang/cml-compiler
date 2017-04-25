package cml.language.foundation;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public interface PropertyList extends Scope
{
    default List<Property> getProperties()
    {
        return getElements().stream()
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

