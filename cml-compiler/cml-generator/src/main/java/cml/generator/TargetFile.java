package cml.generator;

import cml.templates.TemplateFile;

import java.util.Optional;

class TargetFile
{
    private static final char EXTENSION_SEPARATOR = '.';

    TargetFile(final String path, final String templateName)
    {
        this.path = path;
        this.templateName = templateName;
    }

    // Attributes:

    private final String path;

    String getPath()
    {
        return path;
    }

    String getExtension()
    {
        return path.substring(path.lastIndexOf(EXTENSION_SEPARATOR) + 1);
    }

    //---

    private final String templateName;

    String getTemplateName()
    {
        return templateName;
    }

    // Associations:

    private TemplateFile templateFile;

    Optional<TemplateFile> getTemplateFile()
    {
        return Optional.ofNullable(templateFile);
    }

    void setTemplateFile(final TemplateFile templateFile)
    {
        this.templateFile = templateFile;
    }
}
