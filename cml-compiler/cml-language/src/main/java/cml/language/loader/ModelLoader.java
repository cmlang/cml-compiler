package cml.language.loader;

import cml.io.Console;
import cml.io.Directory;
import cml.io.ModuleManager;
import cml.io.SourceFile;
import cml.language.features.Import;
import cml.language.features.Module;
import cml.language.foundation.Diagnostic;
import cml.language.foundation.Model;
import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.grammar.CMLLexer;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.CompilationUnitContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static cml.language.functions.ModelElementFunctions.diagnosticIdentificationOf;
import static cml.language.functions.ModelFunctions.moduleOf;
import static cml.language.functions.ModelVisitorFunctions.visitModel;
import static cml.language.functions.ModuleFunctions.importedModuleOf;

public interface ModelLoader
{
    int loadModel(Model model, String moduleName);

    static ModelLoader create(Console console, ModuleManager moduleManager)
    {
        return new ModelLoaderImpl(console, moduleManager);
    }
}

class ModelLoaderImpl implements ModelLoader
{
    private static final String MAIN_SOURCE = "main.cml";
    private static final String CML_BASE_MODULE = "cml_base";

    private static final int SUCCESS = 0;
    private static final int FAILURE__SOURCE_FILE_NOT_FOUND = 2;
    private static final int FAILURE__FAILED_LOADING_MODEL = 3;
    private static final int FAILURE__MODEL_VALIDATION = 4;

    private static final String NO_MAIN_SOURCE_FILE_IN_MODULE = "no main source file in module: %s";
    private static final String NO_SOURCE_DIR_IN_MODULE = "no source dir in module: %s";

    private final Console console;
    private final ModuleManager moduleManager;

    ModelLoaderImpl(final Console console, final ModuleManager moduleManager)
    {
        this.console = console;
        this.moduleManager = moduleManager;
    }

    @Override
    public int loadModel(Model model, String moduleName)
    {
        try
        {
            final int exitCode = loadModule(model, moduleName, null);

            if (exitCode == SUCCESS)
            {
                linkFunctions(model);
                linkLambdaScope(model);

                return validateModel(model);
            }
            
            return exitCode;
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

    private int loadModule(Model model, String moduleName, @Nullable Import _import) throws IOException
    {
        final Optional<Module> existingModule = moduleOf(model, moduleName);
        if (existingModule.isPresent())
        {
            assert _import != null;

            _import.setImportedModule(existingModule.get());

            return SUCCESS;
        }

        final Module module = Module.create(model, moduleName);

        if (_import != null)
        {
            _import.setImportedModule(module);
        }

        final Optional<Directory> sourceDir = moduleManager.findSourceDir(moduleName);
        if (sourceDir.isPresent())
        {
            final Optional<SourceFile> sourceFile = moduleManager.findSourceFile(moduleName, MAIN_SOURCE);
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
                int exitCode = loadModule(model, i.getName(), i);

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
        if (!module.getName().equals(CML_BASE_MODULE) && !importedModuleOf(module, CML_BASE_MODULE).isPresent())
        {
            Import.create(module, CML_BASE_MODULE);
        }
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

    private void linkFunctions(final Model model)
    {
        visitModel(model, new FunctionLinker());
    }

    private void linkLambdaScope(final Model model)
    {
        visitModel(model, new LambdaScopeLinker());
    }

    private int validateModel(Model model)
    {
        final ModelValidator modelValidator = new ModelValidator();

        visitModel(model, modelValidator);

        if (modelValidator.getDiagnostics().size() == 0)
        {
            return SUCCESS;
        }
        else
        {
            for (Diagnostic diagnostic: modelValidator.getDiagnostics())
            {
                console.print(
                    "\nFailed validation: required %s: in %s",
                    diagnostic.getCode(),
                    diagnosticIdentificationOf(diagnostic.getElement()));

                printLocation(diagnostic.getElement());

                if (diagnostic.getMessage().isPresent())
                {
                    console.println(diagnostic.getMessage().get());
                }

                for(ModelElement element: diagnostic.getParticipants())
                {
                    console.print("- %s", diagnosticIdentificationOf(element));

                    printLocation(element);
                }
            }

            return FAILURE__MODEL_VALIDATION;
        }
    }

    private void printLocation(ModelElement element)
    {
        if (element.getLocation().isPresent())
        {
            final Location location = element.getLocation().get();

            console.println(" (%d:%d)", location.getLine(), location.getColumn());
        }
        else
        {
            console.println("");
        }
    }

    private CompilationUnitContext parse(SourceFile sourceFile) throws IOException
    {
        try (final FileInputStream fileInputStream = new FileInputStream(sourceFile.getPath()))
        {
            final ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
            final CMLLexer lexer = new CMLLexer(input);
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final CMLParser parser = new CMLParser(tokens);
            final SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener(console);

            parser.getErrorListeners().clear();
            parser.addErrorListener(syntaxErrorListener);

            return parser.compilationUnit();
        }
    }
}
