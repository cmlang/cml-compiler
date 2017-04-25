package cml.frontend;

public final class Launcher
{
    private static final String SOURCE_DIR = "./source";
    private static final String TARGETS_DIR = "./targets";

    private Launcher()
    {
    }

    public static void main(final String... args)
    {
        final String targetType = args[0];
        final String sourceDirPath = SOURCE_DIR;
        final String targetDirPath = TARGETS_DIR + "/" + targetType;

        final Compiler compiler = Compiler.create();
        final int exitCode = compiler.compile(sourceDirPath, targetDirPath, targetType);

        System.exit(exitCode);
    }
}
