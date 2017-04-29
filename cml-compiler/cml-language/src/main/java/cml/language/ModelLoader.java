package cml.language;

import cml.io.Console;
import cml.io.Directory;
import cml.io.FileSystem;
import cml.io.SourceFile;
import cml.language.features.Import;
import cml.language.features.Module;
import cml.language.grammar.CMLLexer;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.CompilationUnitContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public interface ModelLoader
{
    int loadModel(Model model, String modulePath);

    static ModelLoader create(Console console, FileSystem fileSystem)
    {
        return new ModelLoaderImpl(console, fileSystem);
    }
}

class ModelLoaderImpl implements ModelLoader
{
    private static final String SOURCE_DIR = "/source";
    private static final String MAIN_SOURCE = "main.cml";
    private static final String CML_BASE_MODULE = "cml_base";

    private static final int SUCCESS = 0;
    private static final int FAILURE__SOURCE_FILE_NOT_FOUND = 2;
    private static final int FAILURE__FAILED_LOADING_MODEL = 3;

    private static final String NO_MAIN_SOURCE_FILE_IN_MODULE = "no main source file in module: %s";
    private static final String NO_SOURCE_DIR_IN_MODULE = "no source dir in module: %s";

    private final Console console;
    private final FileSystem fileSystem;

    ModelLoaderImpl(final Console console, final FileSystem fileSystem)
    {
        this.console = console;
        this.fileSystem = fileSystem;
    }

    @Override
    public int loadModel(Model model, String modulePath)
    {
        try
        {
            final String basePath = fileSystem.extractParentPath(modulePath);
            final String moduleName = fileSystem.extractName(modulePath);

            return loadModule(model, basePath, moduleName, null);
        }
        catch (final Throwable exception)
        {
            if (exception.getMessage() == null)
            {
                 console.error("Unable to parse source files.");
            }
            else
            {
                console.error(exception.getMessage());
            }

            if (!(exception instanceof ModelLoadingException))
            {
                exception.printStackTrace(System.err);
            }

            return FAILURE__FAILED_LOADING_MODEL;
        }
    }

    private int loadModule(Model model, String basePath, String moduleName, Import _import) throws IOException
    {
        final Optional<Module> existingModule = model.getModule(moduleName);
        if (existingModule.isPresent())
        {
            _import.setModule(existingModule.get());

            return SUCCESS;
        }

        final Module module = createModule(model, moduleName, _import);

        if (_import != null)
        {
            _import.setModule(module);
        }

        final String sourceDirPath = basePath + File.separator + moduleName + File.separator + SOURCE_DIR;
        final Optional<Directory> sourceDir = fileSystem.findDirectory(sourceDirPath);
        if (sourceDir.isPresent())
        {
            final Optional<SourceFile> sourceFile = fileSystem.findSourceFile(sourceDir.get(), MAIN_SOURCE);
            if (!sourceFile.isPresent())
            {
                console.error(NO_MAIN_SOURCE_FILE_IN_MODULE, moduleName);
                return FAILURE__SOURCE_FILE_NOT_FOUND;
            }

            final CompilationUnitContext compilationUnitContext = parse(sourceFile.get());

            synthesizeModule(module, compilationUnitContext);

            addBaseModule(module);

            for (Import i: module.getImports())
            {
                int exitCode = loadModule(model, basePath, i.getName(), i);

                if (exitCode != SUCCESS)
                {
                    return exitCode;
                }
            }

            augmentModule(module, compilationUnitContext);
        }
        else
        {
            console.info(NO_SOURCE_DIR_IN_MODULE, moduleName);
        }

        return SUCCESS;
    }

    private void addBaseModule(Module module)
    {
        if (!module.getName().equals(CML_BASE_MODULE) && !module.getImportedModule(CML_BASE_MODULE).isPresent())
        {
            module.addElement(Import.create(CML_BASE_MODULE));
        }
    }

    private Module createModule(Model model, String moduleName, Import _import)
    {
        final Module module = Module.create(moduleName);

        model.addElement(module);

        return module;
    }

    private void synthesizeModule(Module module, CompilationUnitContext compilationUnitContext)
    {
        final ParseTreeWalker walker = new ParseTreeWalker();
        final ModelSynthesizer modelSynthesizer = new ModelSynthesizer(module);

        walker.walk(modelSynthesizer, compilationUnitContext);
    }

    private void augmentModule(Module module, CompilationUnitContext compilationUnitContext)
    {
        final ParseTreeWalker walker = new ParseTreeWalker();
        final ModelAugmenter modelAugmenter = new ModelAugmenter(module);

        walker.walk(modelAugmenter, compilationUnitContext);
    }

    private CompilationUnitContext parse(SourceFile sourceFile) throws IOException
    {
        try (final FileInputStream fileInputStream = new FileInputStream(sourceFile.getPath()))
        {
            final ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
            final CMLLexer lexer = new CMLLexer(input);
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final CMLParser parser = new CMLParser(tokens);
            
            return parser.compilationUnit();
        }
    }
}
