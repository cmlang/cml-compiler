package cml.frontend;

import cml.generator.Generator;
import cml.io.Console;
import cml.io.FileSystem;
import cml.io.ModuleManager;
import cml.language.foundation.Model;
import cml.language.loader.ModelLoader;

public interface Compiler
{
    int compile(String modulePath, String targetName);

    static Compiler createCompiler()
    {
        final Console console = Console.createSystemConsole();
        return createCompiler(console);
    }

    static Compiler createCompiler(final Console console)
    {
        final FileSystem fileSystem = FileSystem.create(console);
        final ModelLoader modelLoader = ModelLoader.create(console, fileSystem);
        final ModuleManager moduleManager = ModuleManager.create(console, fileSystem);
        final Generator generator = Generator.create(console, fileSystem, moduleManager);

        return new CompilerImpl(fileSystem, moduleManager, modelLoader, generator);
    }
}

class CompilerImpl implements Compiler
{
    private static final String TARGETS_DIR = "/targets";

    private final FileSystem fileSystem;
    private final ModuleManager moduleManager;
    private final ModelLoader modelLoader;
    private final Generator generator;

    CompilerImpl(FileSystem fileSystem, ModuleManager moduleManager, ModelLoader modelLoader, Generator generator)
    {
        this.fileSystem = fileSystem;
        this.moduleManager = moduleManager;
        this.modelLoader = modelLoader;
        this.generator = generator;
    }

    @Override
    public int compile(final String modulePath, final String targetName)
    {
        final String modulesBaseDir = fileSystem.extractParentPath(modulePath);
        
        moduleManager.clearBaseDirs();
        moduleManager.addBaseDir(System.getenv("CML_MODULES_PATH"));
        moduleManager.addBaseDir(modulesBaseDir);

        final Model model = Model.create();
        final int exitCode = modelLoader.loadModel(model, modulePath);

        if (exitCode == 0)
        {
            return generator.generate(model, targetName, modulePath + TARGETS_DIR + "/" + targetName);
        }

        return exitCode;
    }
}

