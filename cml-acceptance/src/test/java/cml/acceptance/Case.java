package cml.acceptance;

import static java.lang.String.format;

class Case
{
    static final String CASES_DIR = "cases";
    static final String COMPILER_OUTPUT_FILENAME = "compiler-output.txt";

    private static final String CLIENT_PATH = "/%s-clients/%s";
    private static final String CLIENT_OUTPUT_FILENAME = "client-output.txt";

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

    String getModulePath()
    {
        return CASES_DIR + "/" + getModuleName();
    }

    String getTargetDirPath()
    {
        return getModulePath() + "/targets/" + getTargetType();
    }

    String getOutputBasePath()
    {
        return getModulePath() + "/" + getTargetLanguageExtension() + "-";
    }

    String getExpectedCompilerOutputPath()
    {
        return getOutputBasePath() + COMPILER_OUTPUT_FILENAME;
    }

    String getExpectedClientOutputPath()
    {
        return getOutputBasePath() + CLIENT_OUTPUT_FILENAME;
    }
}
