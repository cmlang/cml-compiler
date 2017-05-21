package cml.io;

public interface Console
{
    void print(String message, Object... args);
    void println(String message, Object... args);
    void info(final String message, final Object... args);
    void error(final String message, final Object... args);

    static Console create()
    {
        return new ConsoleImpl();
    }
}

@SuppressWarnings("UseOfSystemOutOrSystemErr")
class ConsoleImpl implements Console
{
    @Override
    public void print(final String message, final Object... args)
    {
        System.out.print(String.format(message, args));
    }

    @Override
    public void println(final String message, final Object... args)
    {
        System.out.println(String.format(message, args));
    }

    @Override
    public void info(String message, Object... args)
    {
//        System.out.println(String.format("Info: " + message, args));
    }

    @Override
    public void error(final String message, final Object... args)
    {
        System.out.println(String.format("Error: " + message, args));
    }
}
