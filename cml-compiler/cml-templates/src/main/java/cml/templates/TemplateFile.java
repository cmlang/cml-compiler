package cml.templates;

public class TemplateFile
{
    private final String moduleName;
    private final String path;

    TemplateFile(String moduleName, final String path)
    {
        this.moduleName = moduleName;
        this.path = path;
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public String getPath()
    {
        return path;
    }
}
