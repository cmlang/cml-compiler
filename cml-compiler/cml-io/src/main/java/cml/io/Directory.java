package cml.io;

import java.io.File;

public class Directory
{
    private final String path;

    Directory(final String path)
    {
        this.path = path;
    }

    public String getName()
    {
        return new File(path).getName();
    }

    public String getPath()
    {
        return path;
    }
}
