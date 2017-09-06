package cml.frontend;

public final class Launcher
{
    private static final int EXIT_CODE__EXPECTED_ARGUMENTS = 51;

    private Launcher() {}

    public static void main(final String... args)
    {
        if (args.length == 1)
        {
            final String targetName = args[0];
            final Compiler compiler = Compiler.create();

            if (targetName.equalsIgnoreCase("test"))
            {
                final Tester tester = new Tester(compiler);

                final int exitCode = tester.test();

                System.exit(exitCode);
            }
            else
            {
                final String modulePath = ".";

                final int exitCode = compiler.compile(modulePath, targetName);

                System.exit(exitCode);
            }
        }
        else
        {
            System.out.println("Expected one argument: target name or 'test'");

            System.exit(EXIT_CODE__EXPECTED_ARGUMENTS);
        }
    }
}
