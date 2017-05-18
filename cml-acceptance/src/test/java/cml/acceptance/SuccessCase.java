package cml.acceptance;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import static java.lang.String.format;

class SuccessCase
{
    static final String CASES_DIR = "cases";

    private static final String CLIENT_PATH = "/%s-clients/%s";

    private static final String COMPILER_OUTPUT_FILENAME = "output-%s-compiler-%s.txt";
    private static final String CLIENT_OUTPUT_FILENAME = "output-%s-client-%s.txt";

    private final String moduleName;
    private final String clientName;
    private final String taskName;
    private final String targetLanguageExtension;
    private final @Nullable String pythonModuleName;

    SuccessCase(String moduleName, String clientName, String taskName, String targetLanguageExtension)
    {
        this(moduleName, clientName, taskName, targetLanguageExtension, moduleName.replace("-", "_"));
    }

    SuccessCase(String moduleName, String clientName, String taskName, String targetLanguageExtension, String pythonModuleName)
    {
        this.moduleName = moduleName;
        this.clientName = clientName;
        this.taskName = taskName;
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

    String getTaskName()
    {
        return taskName;
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
        return getModulePath() + "/targets/" + getTaskName();
    }

    String getExpectedCompilerOutputPath()
    {
        return getModulePath() + File.separator + getExpectedCompilerOutputFilename();
    }

    private String getExpectedCompilerOutputFilename()
    {
        return format(COMPILER_OUTPUT_FILENAME, getTargetLanguageExtension(), getTaskName());
    }

    String getExpectedClientOutputPath()
    {
        return getModulePath() + File.separator + getExpectedClientOutputFilename();
    }

    private String getExpectedClientOutputFilename()
    {
        return format(CLIENT_OUTPUT_FILENAME, getTargetLanguageExtension(), getClientName());
    }

}
