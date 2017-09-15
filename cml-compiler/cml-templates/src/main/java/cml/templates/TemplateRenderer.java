package cml.templates;

import cml.io.Console;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Map;
import java.util.Map.Entry;

public interface TemplateRenderer
{
    String renderTemplate(TemplateFile templateFile, String templateName, Map<String, Object> args);

    static TemplateRenderer create(Console console)
    {
        return new TemplateRendererImpl(console);
    }
}

class TemplateRendererImpl implements TemplateRenderer
{
    private static final String UNABLE_TO_LOAD_TEMPLATE = "Unable to load template named '%s' from file: %s";

    private final Console console;

    TemplateRendererImpl(Console console)
    {
        this.console = console;
    }

    @Override
    public String renderTemplate(TemplateFile templateFile, String templateName, Map<String, Object> args)
    {
        final STGroupFile groupFile = new TemplateGroupFile(templateFile.getPath());

        final ST template = groupFile.getInstanceOf(templateName);

        if (template == null)
        {
            console.info(UNABLE_TO_LOAD_TEMPLATE, templateName, templateFile.getPath());
            
            return "";
        }

        for (final Entry<String, Object> entry : args.entrySet())
        {
            template.add(entry.getKey(), entry.getValue());
        }

        return template.render();
    }
}

