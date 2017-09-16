package cml.language.generated;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Location
{
    int getLine();

    int getColumn();

    ModelElement getElement();

    static Location createLocation(int line, int column, ModelElement element)
    {
        return new LocationImpl(null, line, column, element);
    }

    static Location extendLocation(@Nullable Location actual_self, int line, int column, ModelElement element)
    {
        return new LocationImpl(actual_self, line, column, element);
    }
}

class LocationImpl implements Location
{
    private static Localization localization;

    private final @Nullable Location actual_self;

    private final int line;
    private final int column;

    LocationImpl(@Nullable Location actual_self, int line, int column, ModelElement element)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.line = line;
        this.column = column;

        localization.link(element, this.actual_self);
    }

    public int getLine()
    {
        return this.line;
    }

    public int getColumn()
    {
        return this.column;
    }

    public ModelElement getElement()
    {
        return localization.elementOf(actual_self).get();
    }

    public String toString()
    {
        return new StringBuilder(Location.class.getSimpleName())
                   .append('(')
                   .append("line=").append(String.format("\"%s\"", getLine())).append(", ")
                   .append("column=").append(String.format("\"%s\"", getColumn()))
                   .append(')')
                   .toString();
    }

    static void setLocalization(Localization association)
    {
        localization = association;
    }

    static
    {
        Localization.init(Location.class);
    }
}