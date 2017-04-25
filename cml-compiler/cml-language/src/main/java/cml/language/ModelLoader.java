package cml.language;

import cml.io.Console;
import cml.io.Directory;
import cml.io.FileSystem;
import cml.io.SourceFile;
import cml.language.foundation.Model;
import cml.language.grammar.CMLLexer;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.CompilationUnitContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public interface ModelLoader
{
    int loadModel(Model model, Directory sourceDir);

    static ModelLoader create(Console console, FileSystem fileSystem)
    {
        return new ModelLoaderImpl(console, fileSystem);
    }
}

class ModelLoaderImpl implements ModelLoader
{
    private static final String MAIN_SOURCE = "main.cml";

    private static final int SUCCESS = 0;
    private static final int FAILURE__PARSING_FAILED = 3;
    private static final int FAILURE__SOURCE_FILE_NOT_FOUND = 2;

    private final Console console;
    private final FileSystem fileSystem;

    ModelLoaderImpl(final Console console, final FileSystem fileSystem)
    {
        this.console = console;
        this.fileSystem = fileSystem;
    }

    @Override
    public int loadModel(Model model, Directory sourceDir)
    {
        final Optional<SourceFile> sourceFile = fileSystem.findSourceFile(sourceDir, MAIN_SOURCE);
        if (!sourceFile.isPresent())
        {
            console.println(
                "Main source file (%s) missing in source dir: %s", MAIN_SOURCE,
                sourceDir.getPath());
            return FAILURE__SOURCE_FILE_NOT_FOUND;
        }

        try (final FileInputStream fileInputStream = new FileInputStream(sourceFile.get().getPath()))
        {
            final CompilationUnitContext compilationUnitContext = parse(fileInputStream);
            final ModelSynthesizer modelSynthesizer = new ModelSynthesizer(model);
            final ModelAugmenter modelInheritor = new ModelAugmenter(model);
            final ParseTreeWalker walker = new ParseTreeWalker();

            walker.walk(modelSynthesizer, compilationUnitContext);
            walker.walk(modelInheritor, compilationUnitContext);

            return SUCCESS;
        }
        catch (final Throwable exception)
        {
            if (exception.getMessage() != null)
            {
                console.println("Parsing Error: %s", exception.getMessage());
            }
            return FAILURE__PARSING_FAILED;
        }
    }

    private CompilationUnitContext parse(FileInputStream fileInputStream) throws IOException
    {
        final ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
        final CMLLexer lexer = new CMLLexer(input);
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final CMLParser parser = new CMLParser(tokens);
        return parser.compilationUnit();
    }
}
