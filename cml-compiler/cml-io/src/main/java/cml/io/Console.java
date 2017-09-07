package cml.io;

import static java.lang.String.format;

public interface Console
{
    default void println()
    {
        println("");
    }

    void print(String message, Object... args);
    void println(String message, Object... args);
    void info(final String message, final Object... args);
    void error(final String message, final Object... args);

    static Console createSystemConsole()
    {
        return new SystemConsole();
    }

    static Console createStringConsole()
    {
        return new StringConsole();
    }
}

class SystemConsole implements Console
{
    @Override
    public void print(final String message, final Object... args)
    {
        System.out.print(format(message, args));
    }

    @Override
    public void println(final String message, final Object... args)
    {
        System.out.println(format(message, args));
    }

    @Override
    public void info(String message, Object... args)
    {
//        System.out.println(String.format("Info: " + message, args));
    }

    @Override
    public void error(final String message, final Object... args)
    {
        System.out.println(format("Error: " + message, args));
    }
}

class StringConsole implements Console
{
    private StringBuilder str = new StringBuilder();

    @Override
    public void print(final String message, final Object... args)
    {
        str.append(format(message, args));
    }

    @Override
    public void println(final String message, final Object... args)
    {
        str.append(format(message, args)).append('\n');
    }

    @Override
    public void info(String message, Object... args)
    {
    }

    @Override
    public void error(final String message, final Object... args)
    {
        str.append(format("Error: " + message, args)).append('\n');
    }

    @Override
    public String toString()
    {
        return str.toString();
    }
}
