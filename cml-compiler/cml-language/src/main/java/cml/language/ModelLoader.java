package cml.language;

import cml.io.Console;
import cml.io.SourceFile;
import cml.language.grammar.CMLLexer;
import cml.language.grammar.CMLParser;
import cml.language.grammar.CMLParser.CompilationUnitContext;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.empty;

public interface ModelLoader
{
    Optional<CompilationUnitContext> loadModel(SourceFile sourceFile);

    static ModelLoader create(Console console)
    {
        return new ModelLoaderImpl(console);
    }
}

class ModelLoaderImpl implements ModelLoader
{
    private final Console console;

    ModelLoaderImpl(final Console console)
    {
        this.console = console;
    }

    @Override
    public Optional<CompilationUnitContext> loadModel(SourceFile sourceFile)
    {
        try (final FileInputStream fileInputStream = new FileInputStream(sourceFile.getPath()))
        {
            final ANTLRInputStream input = new ANTLRInputStream(fileInputStream);
            final CMLLexer lexer = new CMLLexer(input);
            final CommonTokenStream tokens = new CommonTokenStream(lexer);
            final CMLParser parser = new CMLParser(tokens);
            final CompilationUnitContext compilationUnitContext = parser.compilationUnit();
            final ParseTreeWalker walker = new ParseTreeWalker();
            final ModelSynthesizer modelSynthesizer = new ModelSynthesizer();
            final ModelAugmenter modelInheritor = new ModelAugmenter();

            walker.walk(modelSynthesizer, compilationUnitContext);
            walker.walk(modelInheritor, compilationUnitContext);

            return Optional.of(compilationUnitContext);
        }
        catch (final IOException exception)
        {
            console.println("I/O Error: %s", exception.getMessage());
            return empty();
        }
    }
}
