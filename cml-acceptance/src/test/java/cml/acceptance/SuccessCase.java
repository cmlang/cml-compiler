package cml.acceptance;

import static java.lang.String.format;

class SuccessCase
{
    static final String CASES_DIR = "cases";
    static final String COMPILER_OUTPUT_FILENAME = "compiler-output.txt";

    private static final String CLIENT_PATH = "/%s-clients/%s";
    private static final String CLIENT_OUTPUT_FILENAME = "client-output.txt";

    private final String moduleName;
    private final String clientName;
    private final String targetName;
    private final String targetLanguageExtension;

    SuccessCase(String moduleName, String clientName, String targetName, String targetLanguageExtension)
    {
        this.moduleName = moduleName;
        this.clientName = clientName;
        this.targetName = targetName;
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

    String getTargetName()
    {
        return targetName;
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
        return CASES_DIR + "/success/" + getModuleName();
    }

    String getTargetDirPath()
    {
        return getModulePath() + "/targets/" + getTargetName();
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
