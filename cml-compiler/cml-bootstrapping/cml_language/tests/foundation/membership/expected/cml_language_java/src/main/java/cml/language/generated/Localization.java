package cml.language.generated;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

public class Localization
{
    private static final Localization singleton = new Localization();

    static void init(Class<?> cls)
    {
        if (Location.class.isAssignableFrom(cls))
        {
            LocationImpl.setLocalization(singleton);
        }
        if (ModelElement.class.isAssignableFrom(cls))
        {
            ModelElementImpl.setLocalization(singleton);
        }
    }

    private final Map<Location, ModelElement> element = new HashMap<>();
    private final Map<ModelElement, @Nullable Location> location = new HashMap<>();

    synchronized void link(ModelElement modelElement, Location location)
    {
        this.element.put(location, modelElement);

        this.location.put(modelElement, location);
    }

    synchronized void link(Location location, ModelElement modelElement)
    {
        this.element.put(location, modelElement);

        this.location.put(modelElement, location);
    }

    synchronized Optional<ModelElement> elementOf(Location location)
    {
        return Optional.ofNullable(this.element.get(location));
    }

    synchronized Optional<Location> locationOf(ModelElement modelElement)
    {
        return Optional.ofNullable(this.location.get(modelElement));
    }
}