package cml.frontend;

import cml.generator.Generator;
import cml.io.Console;
import cml.io.FileSystem;
import cml.language.Model;
import cml.language.ModelLoader;

public interface Compiler
{
    int compile(String modulePath, String targetName);

    static Compiler create()
    {
        final Console console = Console.create();
        final FileSystem fileSystem = FileSystem.create();
        final ModelLoader modelLoader = ModelLoader.create(console, fileSystem);
        final Generator generator = Generator.create(console, fileSystem);
        return new CompilerImpl(modelLoader, generator);
    }
}

class CompilerImpl implements Compiler
{
    private static final String TARGETS_DIR = "/targets";

    private final ModelLoader modelLoader;
    private final Generator generator;

    CompilerImpl(ModelLoader modelLoader, Generator generator)
    {
        this.modelLoader = modelLoader;
        this.generator = generator;
    }

    @Override
    public int compile(final String modulePath, final String targetName)
    {
        final Model model = Model.create();
        final int exitCode = modelLoader.loadModel(model, modulePath);

        if (exitCode == 0)
        {
            return generator.generate(model, targetName, modulePath + TARGETS_DIR + "/" + targetName);
        }

        return exitCode;
    }
}

