package cml.acceptance;

import static java.lang.String.format;

class Case
{
    private static final String CLIENT_PATH = "/%s-clients/%s";
    
    private final String moduleName;
    private final String clientName;
    private final String targetType;
    private final String targetLanguageExtension;

    Case(String moduleName, String clientName, String targetType, String targetLanguageExtension)
    {
        this.moduleName = moduleName;
        this.clientName = clientName;
        this.targetType = targetType;
        this.targetLanguageExtension = targetLanguageExtension;
    }

    String getModuleName()
    {
        return moduleName;
    }

    String getModuleDir()
    {
        return moduleName.replace("-", "_");
    }

    String getTargetType()
    {
        return targetType;
    }

    String getTargetLanguageExtension()
    {
        return targetLanguageExtension;
    }

    String getClientName()
    {
        return clientName;
    }

    String getClientPath()
    {
        return format(CLIENT_PATH, targetLanguageExtension, clientName);
    }
}
