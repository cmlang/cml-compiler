package cml.templates;

import cml.io.Console;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static java.lang.String.format;

public interface TemplateRenderer
{
    default String renderTemplate(TemplateFile templateFile, String templateName, Map<String, Object> args)
    {
        return renderTemplate(templateFile, templateName, args, null);
    }

    String renderTemplate(
        TemplateFile templateFile,
        String templateName,
        Map<String, Object> args,
        @Nullable String targetLanguageExtension);

    static TemplateRenderer create(Console console)
    {
        return new TemplateRendererImpl(console);
    }
}

class TemplateRendererImpl implements TemplateRenderer
{
    private static final String LANGUAGE_GROUP = "cml_base:/lang/%s.stg";

    private final Console console;

    TemplateRendererImpl(Console console)
    {
        this.console = console;
    }

    @Override
    public String renderTemplate(
        TemplateFile templateFile,
        String templateName,
        Map<String, Object> args,
        @Nullable String targetLanguageExtension)
    {
        final STGroupFile groupFile = new TemplateGroupFile(templateFile.getPath());

        if (targetLanguageExtension != null)
        {
            final Optional<TemplateGroupFile> languageTemplates = loadLanguageTemplates(targetLanguageExtension);

            languageTemplates.ifPresent(groupFile::importTemplates);
        }

        final ST template = groupFile.getInstanceOf(templateName);

        if (template == null)
        {
            printErrorMessage(templateFile, templateName);
            return "";
        }

        for (final Entry<String, Object> entry : args.entrySet())
        {
            template.add(entry.getKey(), entry.getValue());
        }

        return template.render();
    }

    private void printErrorMessage(TemplateFile templateFile, String templateName)
    {
        console.println(
            "Unable to load template named '%s' from file: %s",
            templateName, templateFile.getPath());
    }

    private static Optional<TemplateGroupFile> loadLanguageTemplates(@Nullable String targetLanguageExtension)
    {
        try
        {
            return Optional.of(new TemplateGroupFile(format(LANGUAGE_GROUP, targetLanguageExtension)));
        }
        catch (IllegalArgumentException ignored)
        {
            // ignore - no language group for file type.
            return Optional.empty();
        }
    }
}

