package cml.language.loader;

import cml.io.Console;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class SyntaxErrorListener extends BaseErrorListener
{
    private final Console console;

    SyntaxErrorListener(final Console console)
    {
        this.console = console;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String defaultErrorMessage, RecognitionException exception)
    {
        console.println("Syntax Error: %s (%s:%s)", defaultErrorMessage, line, charPositionInLine + 1);
    }
}