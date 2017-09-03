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

    static Location createLocation(int line, int column)
    {
        return new LocationImpl(line, column);
    }

    static Location extendLocation(int line, int column)
    {
        return new LocationImpl(line, column);
    }
}

class LocationImpl implements Location
{
    private final int line;
    private final int column;

    public LocationImpl(int line, int column)
    {
        this.line = line;
        this.column = column;
    }

    public int getLine()
    {
        return this.line;
    }

    public int getColumn()
    {
        return this.column;
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
}