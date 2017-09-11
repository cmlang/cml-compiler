package cml.templates;

import cml.io.ModuleManager;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.misc.ErrorManager;
import org.stringtemplate.v4.misc.Misc;
import org.stringtemplate.v4.misc.STLexerMessage;

import java.net.URL;
import java.util.Optional;

public class TemplateGroupFile extends STGroupFile
{
    private static final String ENCODING = "UTF-8";
    private static final char START_CHAR = '<';
    private static final char STOP_CHAR = '>';

    private static ModuleManager moduleManager;

    public static void setModuleManager(ModuleManager moduleManager)
    {
        TemplateGroupFile.moduleManager = moduleManager;
    }

    public TemplateGroupFile(String path)
    {
        super(path, ENCODING, START_CHAR, STOP_CHAR);

        errMgr = new ErrorManager()
        {
            @Override
            public void lexerError(final String srcName, final String msg, final Token templateToken, final RecognitionException e)
            {
                // Overriding to display the full srcName:
                listener.compileTimeError(new STLexerMessage(srcName, msg, templateToken, e));
            }
        };

        registerModelAdaptor(Object.class, new ModelAdaptor());
        registerRenderer(String.class, new NameRenderer());
    }

    @Override
    public URL getURL(String path)
    {
        final Optional<URL> templateFile = moduleManager.findTemplateFile(path);
        return templateFile.orElse(null);
    }

    @Override
    public void importTemplates(Token fileNameToken)
    {
        final String moduleName = moduleManager.getModuleName(getFileName());
        final String importFileName = Misc.strip(fileNameToken.getText(), 1);
        final String path = moduleManager.getModulePath(moduleName, importFileName);
        final STGroup g = new TemplateGroupFile(path);

        g.setListener(getListener());

        importTemplates(g, true);
    }
}
