package cml.frontend;

public final class Launcher
{
    private Launcher()
    {
    }

    public static void main(final String... args)
    {
        final String modulePath = ".";
        final String targetName = args[0];

        final Compiler compiler = Compiler.create();
        final int exitCode = compiler.compile(modulePath, targetName);

        System.exit(exitCode);
    }
}
