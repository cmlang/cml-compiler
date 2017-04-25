package cml.frontend;

import cml.generator.Generator;
import cml.io.Console;
import cml.io.Directory;
import cml.io.FileSystem;
import cml.language.ModelLoader;
import cml.language.foundation.Model;

import java.util.Optional;

public interface Compiler
{
    int compile(String sourceDirPath, String targetDirPath, String targetName);

    static Compiler create()
    {
        final Console console = Console.create();
        final FileSystem fileSystem = FileSystem.create();
        final ModelLoader modelLoader = ModelLoader.create(console, fileSystem);
        final Generator generator = Generator.create(console, fileSystem);
        return new CompilerImpl(console, fileSystem, modelLoader, generator);
    }
}

class CompilerImpl implements Compiler
{
    private static final int FAILURE__SOURCE_DIR_NOT_FOUND = 1;

    private final Console console;
    private final FileSystem fileSystem;
    private final ModelLoader modelLoader;
    private final Generator generator;

    CompilerImpl(Console console, FileSystem fileSystem, ModelLoader modelLoader, Generator generator)
    {
        this.console = console;
        this.fileSystem = fileSystem;
        this.modelLoader = modelLoader;
        this.generator = generator;
    }

    @Override
    public int compile(final String sourceDirPath, final String targetDirPath, final String targetName)
    {
        final Optional<Directory> sourceDir = fileSystem.findDirectory(sourceDirPath);
        if (!sourceDir.isPresent())
        {
            console.println("Source dir missing: %s", sourceDirPath);
            return FAILURE__SOURCE_DIR_NOT_FOUND;
        }

        final Model model = Model.create();
        final int exitCode = modelLoader.loadModel(model, sourceDir.get());

        if (exitCode == 0)
        {
            return generator.generate(model, targetName, targetDirPath);
        }
        else
        {
            console.println("Unable to parse source files.");
            return exitCode;
        }
    }
}

