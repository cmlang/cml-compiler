package cml.frontend;

import cml.generator.Generator;
import cml.io.Console;
import cml.io.Directory;
import cml.io.FileSystem;
import cml.io.SourceFile;
import cml.language.ModelLoader;
import cml.language.foundation.Model;

import java.util.Optional;

public interface Compiler
{
    int compile(String sourceDirPath, String targetDirPath, String targetType);

    static Compiler create()
    {
        final Console console = Console.create();
        final FileSystem fileSystem = FileSystem.create();
        final ModelLoader modelLoader = ModelLoader.create(console);
        final Generator generator = Generator.create(console, fileSystem);
        return new CompilerImpl(console, fileSystem, modelLoader, generator);
    }
}

class CompilerImpl implements Compiler
{
    private static final String MAIN_SOURCE = "main.cml";

    private static final int FAILURE__SOURCE_DIR_NOT_FOUND = 1;
    private static final int FAILURE__SOURCE_FILE_NOT_FOUND = 2;
    private static final int FAILURE__PARSING_FAILED = 3;

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
    public int compile(final String sourceDirPath, final String targetDirPath, final String targetType)
    {
        final Optional<Directory> sourceDir = fileSystem.findDirectory(sourceDirPath);
        if (!sourceDir.isPresent())
        {
            console.println("Source dir missing: %s", sourceDirPath);
            return FAILURE__SOURCE_DIR_NOT_FOUND;
        }

        final Optional<SourceFile> sourceFile = fileSystem.findSourceFile(sourceDir.get(), MAIN_SOURCE);
        if (!sourceFile.isPresent())
        {
            console.println(
                "Main source file (%s) missing in source dir: %s", MAIN_SOURCE,
                sourceDir.get().getPath());
            return FAILURE__SOURCE_FILE_NOT_FOUND;
        }

        final Optional<Model> model = modelLoader.loadModel(sourceFile.get());
        if (model.isPresent())
        {
            return generator.generate(model.get(), targetType, targetDirPath);
        }
        else
        {
            console.println("Unable to parse source files.");
            return FAILURE__PARSING_FAILED;
        }
    }
}

