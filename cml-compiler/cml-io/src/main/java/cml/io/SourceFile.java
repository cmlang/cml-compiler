package cml.io;

public class SourceFile implements Comparable<SourceFile>
{
    private static final char EXTENSION_SEPARATOR = '.';

    private final String path;

    SourceFile(final String path)
    {
        this.path = path;
    }

    public String getPath()
    {
        return path;
    }

    String getExtension()
    {
        return path.substring(path.lastIndexOf(EXTENSION_SEPARATOR) + 1);
    }

    @Override
    public int compareTo(final SourceFile other)
    {
        return path.compareTo(other.path);
    }
}
