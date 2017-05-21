package cml.language.foundation;

import static java.lang.String.format;

public interface Location
{
    int getLine();
    int getColumn();

    static Location createLocation(int line, int column)
    {
        return new LocationImpl(line, column);
    }
}

class LocationImpl implements Location
{
    private final int line;
    private final int column;

    LocationImpl(int line, int column)
    {
        this.line = line;
        this.column = column;
    }

    @Override
    public int getLine()
    {
        return line;
    }

    @Override
    public int getColumn()
    {
        return column;
    }

    @Override
    public String toString()
    {
        return format("(%d:%d)", line, column);
    }
}
