package cml.acceptance;

import com.sun.istack.internal.Nullable;

import java.io.File;

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
    private final @Nullable String pythonModuleName;

    SuccessCase(String moduleName, String clientName, String targetName, String targetLanguageExtension)
    {
        this(moduleName, clientName, targetName, targetLanguageExtension, moduleName.replace("-", "_"));
    }

    SuccessCase(String moduleName, String clientName, String targetName, String targetLanguageExtension, String pythonModuleName)
    {
        this.moduleName = moduleName;
        this.clientName = clientName;
        this.targetName = targetName;
        this.targetLanguageExtension = targetLanguageExtension;
        this.pythonModuleName = pythonModuleName;
    }

    String getModuleName()
    {
        return moduleName;
    }

    String getPythonModuleDir(String baseDir)
    {
        return baseDir + File.separator + pythonModuleName;
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
