package cml.acceptance;

import static java.lang.String.format;

class Case
{
    private static final String CLIENT_PATH = "/%s-clients/%s";
    
    private final String name;
    private final String targetType;
    private final String clientName;
    private final String languageName;

    Case(String name, String targetType, String languageName, String clientName)
    {
        this.name = name;
        this.targetType = targetType;
        this.languageName = languageName;
        this.clientName = clientName;
    }

    String getName()
    {
        return name;
    }

    String getTargetType()
    {
        return targetType;
    }

    String getClientName()
    {
        return clientName;
    }

    String getClientPath()
    {
        return format(CLIENT_PATH, languageName, clientName);
    }
}
