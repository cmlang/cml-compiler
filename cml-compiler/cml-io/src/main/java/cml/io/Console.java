package cml.io;

public interface Console
{
    void println(String message, Object... args);
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
    public void println(final String message, final Object... args)
    {
        System.out.println(String.format(message, args));
    }

    @Override
    public void error(final String message, final Object... args)
    {
        System.out.println(String.format("Error: " + message, args));
    }
}
