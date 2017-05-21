package cml.acceptance;

import com.google.common.io.Files;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertEquals;
import static org.codehaus.plexus.util.FileUtils.*;
import static org.codehaus.plexus.util.cli.CommandLineUtils.executeCommandLine;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(Theories.class)
public class AcceptanceTest
{
    private static final Charset OUTPUT_FILE_ENCODING = Charset.forName("UTF-8");

    private static final String BASE_DIR = "..";

    private static final String COMPILER_DIR = BASE_DIR + "/cml-compiler";
    private static final String FRONTEND_DIR = COMPILER_DIR + "/cml-frontend";
    private static final String FRONTEND_TARGET_DIR = FRONTEND_DIR + "/target";
    private static final String COMPILER_JAR = FRONTEND_TARGET_DIR + "/cml-compiler-jar-with-dependencies.jar";

    private static final String CLIENT_BASE_DIR = BASE_DIR + "/" + "cml-clients";
    private static final String CLIENT_JAR_SUFFIX = "-jar-with-dependencies.jar";

    private static final String CML_MODULES_BASE_DIR = BASE_DIR + "/" + "cml-modules";

    private static final String POJ = "poj"; // plain old Java

    private static final String JAVA = "java";
    private static final String PYTHON = "py";

    private static final int SUCCESS = 0;
    private static final int FAILURE__SOURCE_FILE_NOT_FOUND = 2;
    private static final int FAILURE__FAILED_LOADING_MODEL = 3;
    private static final int FAILURE__MODEL_VALIDATION = 4;
    private static final int FAILURE__CONSTRUCTOR_UNKNOWN = 101;
    private static final int FAILURE__CONSTRUCTOR_UNDEFINED = 102;
    private static final int FAILURE__TASK_UNDECLARED = 103;
    private static final String SOME_TASK = "some_task";

    @DataPoints("success-cases")
    public static SuccessCase[] successCases = {
        new SuccessCase("expressions", "expressions-console", "expressions_poj", JAVA),
        new SuccessCase("expressions", "expressions_console", "expressions_pop", PYTHON),
        new SuccessCase("livir_books", "livir-console", "livir_poj", JAVA),
        new SuccessCase("livir_books", "livir_console", "livir_pop", PYTHON),
        new SuccessCase("mini_cml_language", "mcml-compiler", "mcml_java", JAVA),
        new SuccessCase("mini_cml_language", "mcml_compiler", "mcml_py", PYTHON),
        new SuccessCase("shapes", "shapes-console", "shapes_java", JAVA),
        new SuccessCase("shapes", "shapes_cmlc_console", "shapes_cmlc_py", PYTHON, "shapes_cmlc"),
//        new SuccessCase("shapes", "shapes_pop_console", "shapes_pop_py", PYTHON, "shapes_pop")
    };

    @DataPoints("validation-modules")
    public static String[] validationModules = {
        "unique_property_name",
        "not_own_generalization",
        "compatible_generalizations",
        "conflict_redefinition",
        "generalization_compatible_redefinition",
        "abstract_property_redefinition",
        "abstract_property_in_abstract_concept"
    };

    @DataPoints("failing-modules")
    public static String[] failingModules = {
        "invalid_module_name",
        "missing_ancestor",
        "missing_concept_in_type",
        "parsing_failed"
    };

    @BeforeClass
    public static void buildCompiler() throws MavenInvocationException
    {
        buildMavenModule(COMPILER_DIR);

        final File targetDir = new File(FRONTEND_TARGET_DIR);
        assertThat("Target dir must exist: " + targetDir, targetDir.exists(), is(true));
    }

    @Theory
    public void success(@FromDataPoints("success-cases") final SuccessCase successCase) throws Exception
    {
        cleanTargetDir(successCase.getModulePath(), successCase.getTaskName());

        compileAndVerifyOutput(
            successCase.getModulePath(),
            successCase.getTaskName(),
            successCase.getExpectedCompilerOutputPath(),
            SUCCESS);
        installGeneratedModule(successCase.getTargetDirPath(), successCase);

        final String actualClientOutput = executeClient(successCase);
        assertThatOutputMatches("client's output", successCase.getExpectedClientOutputPath(), actualClientOutput);
    }

    @Theory
    public void model_validation(@FromDataPoints("validation-modules") final String moduleName) throws Exception
    {
        final String modulePath = getValidationModulePath(moduleName);

        cleanTargetDir(modulePath, SOME_TASK);
        compileWithTaskAndVerifyOutput(modulePath, SOME_TASK, FAILURE__MODEL_VALIDATION);
    }

    @Theory
    public void error_loading_model(@FromDataPoints("failing-modules") final String moduleName) throws Exception
    {
        final String modulePath = getErrorModulePath(moduleName);

        cleanTargetDir(modulePath, SOME_TASK);
        compileWithTaskAndVerifyOutput(modulePath, SOME_TASK, FAILURE__FAILED_LOADING_MODEL);
    }

    @Test
    public void missing_source_file() throws Exception
    {
        final String modulePath = getErrorModulePath("missing-source");

        cleanTargetDir(modulePath, POJ);
        compileAndVerifyOutput(modulePath, POJ, FAILURE__SOURCE_FILE_NOT_FOUND);
    }

    @Test
    public void constructor_unknown() throws Exception
    {
        final String modulePath = getErrorModulePath("constructor_unknown");

        cleanTargetDir(modulePath, "unknown_constructor_task");
        compileWithTaskAndVerifyOutput(modulePath, "unknown_constructor_task", FAILURE__CONSTRUCTOR_UNKNOWN);
    }

    @Test
    public void task_undeclared() throws Exception
    {
        final String modulePath = getErrorModulePath("task_undeclared");

        cleanTargetDir(modulePath, "some_task_name");
        compileAndVerifyOutput(modulePath, "some_task_name", FAILURE__TASK_UNDECLARED);
    }

    @Test
    public void constructor_undefined() throws Exception
    {
        final String modulePath = getErrorModulePath("constructor_undefined");

        cleanTargetDir(modulePath, "some_task");
        compileAndVerifyOutput(modulePath, "some_task", FAILURE__CONSTRUCTOR_UNDEFINED);
    }

    @Test
    public void target_dir_created() throws Exception
    {
        final String modulePath = SuccessCase.CASES_DIR + "/target_dirs/target_dir_created";
        final File targetDir = new File(getTargetDirPath(modulePath, SOME_TASK));

        forceDelete(targetDir);
        assertThat("Target dir must NOT exist: " + targetDir, targetDir.exists(), is(false));

        compileAndVerifyOutput(modulePath, SOME_TASK, SUCCESS);
        assertThat("Target dir must exist: " + targetDir, targetDir.exists(), is(true));
    }

    @Test
    public void target_dir_cleaned() throws Exception
    {
        final String modulePath = SuccessCase.CASES_DIR + "/target_dirs/target_dir_cleaned";
        final File targetDir = new File(getTargetDirPath(modulePath, SOME_TASK));

        final File bookFile = new File(targetDir, "src/main/java/books/Book.java");
        final File bookStoreFile = new File(targetDir, "src/main/java/books/BookStore.java");

        // Ensures there is already content in the target dir:
        final String tempModulePath = SuccessCase.CASES_DIR + "/target_dirs/target_dir_created";
        compileAndVerifyOutput(tempModulePath, SOME_TASK, SUCCESS);
        cleanTargetDir(modulePath, SOME_TASK);
        copyDirectoryStructure(
            new File(getTargetDirPath(tempModulePath, SOME_TASK)),
            new File(getTargetDirPath(modulePath, SOME_TASK)));
        assertThat("Book must exist: " + bookFile, bookFile.exists(), is(true));
        assertThat("BookStore must NOT exist: " + bookFile, bookStoreFile.exists(), is(false));

        // Verifies that the previously generated target has been cleaned before generating the new one:
        compileAndVerifyOutput(modulePath, SOME_TASK, SUCCESS);
        assertThat("Book must NOT exist: " + bookFile, bookFile.exists(), is(false));
        assertThat("BookStore must exist: " + bookFile, bookStoreFile.exists(), is(true));
    }

    private void compileAndVerifyOutput(
        final String modulePath,
        final String taskName,
        final int expectedExitCode) throws CommandLineException, IOException
    {
        compileWithTaskAndVerifyOutput(modulePath, taskName, expectedExitCode);
    }

    private void compileWithTaskAndVerifyOutput(
        final String modulePath,
        final String taskName,
        final int expectedExitCode) throws CommandLineException, IOException
    {
        compileAndVerifyOutput(
            modulePath,
            taskName,
            modulePath + File.separator + "compiler-output.txt",
            expectedExitCode);
    }

    private void compileAndVerifyOutput(
        final String modulePath,
        final String taskName,
        final String expectedOutputPath,
        final int expectedExitCode) throws CommandLineException, IOException
    {
        final String actualCompilerOutput = executeJar(
            modulePath,
            COMPILER_JAR,
            singletonList(taskName),
            expectedExitCode);

        assertThatOutputMatches("compiler's output", expectedOutputPath, actualCompilerOutput);
    }

    private void cleanTargetDir(String currentDirPath, String taskName) throws IOException
    {
        final String targetDirPath = getTargetDirPath(currentDirPath, taskName);

        System.out.println("\n--------------------------");
        System.out.println("Cleaning target dir: " + targetDirPath);

        forceMkdir(new File(targetDirPath));
        cleanDirectory(targetDirPath);
    }

    private String getTargetDirPath(String currentDirPath, String taskName)
    {
        return currentDirPath + "/targets/" + taskName;
    }

    private void assertThatOutputMatches(
        final String reason,
        final String expectedOutputPath,
        final String actualOutput) throws IOException
    {
        final String expectedOutput = Files.toString(new File(expectedOutputPath), OUTPUT_FILE_ENCODING);
        assertEquals(reason, expectedOutput, actualOutput);
    }

    private static void installGeneratedModule(final String baseDir, final SuccessCase successCase)
        throws MavenInvocationException, IOException, CommandLineException
    {
        if (successCase.getTargetLanguageExtension().equals(JAVA))
        {
            buildMavenModule(baseDir);
        }
        else if (successCase.getTargetLanguageExtension().equals(PYTHON))
        {
            checkPythonTypes(successCase.getPythonModuleDir(baseDir));
            installPythonPackage(baseDir);
        }
    }

    private static void buildMavenModule(final String baseDir) throws MavenInvocationException
    {
        System.out.println("Building: " + baseDir);

        System.setProperty("maven.home", System.getenv("M2_HOME"));

        final InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(new File(baseDir));
        request.setGoals(asList("-q", "install"));
        request.setInteractive(false);

        final Invoker invoker = new DefaultInvoker();
        final InvocationResult result = invoker.execute(request);

        if (result.getExitCode() != 0) throw new MavenInvocationException("Exit code: " + result.getExitCode());
    }

    private static void checkPythonTypes(final String moduleDir) throws IOException, CommandLineException
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            // https://github.com/python/mypy
            final Commandline commandLine = new Commandline();
            commandLine.setExecutable(pythonCmd("python3"));
            commandLine.createArg().setValue("-m");
            commandLine.createArg().setValue("mypy");
            commandLine.createArg().setValue(moduleDir);

            final Writer writer = new OutputStreamWriter(outputStream);
            final WriterStreamConsumer systemOut = new WriterStreamConsumer(writer);
            final WriterStreamConsumer systemErr = new WriterStreamConsumer(writer);

            System.out.println("Checking types of Python module: " + commandLine);

            final int exitCode = executeCommandLine(commandLine, systemOut, systemErr, 10);

            System.out.println("mypy's exit code: " + exitCode);
            System.out.println(
                "mypy's output: \n---\n" + new String(outputStream.toByteArray(), OUTPUT_FILE_ENCODING) + "---\n");

            assertThat("mypy's exit code: ", exitCode, is(0));
        }
    }

    private static void installPythonPackage(final String baseDir) throws IOException, CommandLineException
    {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            // --upgrade: overwriting previous installation - https://packaging.python.org/installing
            final Commandline commandLine = new Commandline();
            commandLine.setExecutable(pythonCmd("pip3"));
            commandLine.createArg().setValue("install");
            commandLine.createArg().setValue("--upgrade");
            commandLine.createArg().setValue(baseDir);

            final Writer writer = new OutputStreamWriter(outputStream);
            final WriterStreamConsumer systemOut = new WriterStreamConsumer(writer);
            final WriterStreamConsumer systemErr = new WriterStreamConsumer(writer);

            System.out.println("Installing Python package: " + commandLine);

            final int exitCode = executeCommandLine(commandLine, systemOut, systemErr, 10);

            System.out.println("pip's exit code: " + exitCode);
            System.out.println(
                "pip's output: \n---\n" + new String(outputStream.toByteArray(), OUTPUT_FILE_ENCODING) + "---\n");

            assertThat("pip's exit code: ", exitCode, is(0));
        }
    }

    private static String executeClient(final SuccessCase successCase)
        throws MavenInvocationException, IOException, CommandLineException
    {
        switch (successCase.getTargetLanguageExtension())
        {
            case JAVA:
                return executeJavaClient(successCase);
            case PYTHON:
                return executePythonClient(successCase);
            default:
                return "Unknown language: " + successCase.getTargetLanguageExtension();
        }
    }

    private static String executeJavaClient(SuccessCase successCase)
        throws CommandLineException, IOException, MavenInvocationException
    {
        final String clientModuleDir = CLIENT_BASE_DIR + successCase.getClientPath();
        buildMavenModule(clientModuleDir);

        final File clientTargetDir = new File(clientModuleDir, "target");
        assertThat("Client target dir must exist: " + clientTargetDir, clientTargetDir.exists(), is(true));

        final String clientJarPath = clientTargetDir.getPath() + "/" + successCase.getClientName() + CLIENT_JAR_SUFFIX;

        return executeJar(clientTargetDir.getPath(), clientJarPath, emptyList(), SUCCESS);
    }

    private static String executePythonClient(SuccessCase successCase) throws CommandLineException, IOException
    {
        final String clientPath = CLIENT_BASE_DIR + successCase.getClientPath() + ".py";
        assertThat(
            "Client must exist: " + clientPath,
            new File(clientPath).exists(), is(true));

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try
        {
            // Executing Python module - https://www.python.org/dev/peps/pep-0338/#current-behaviour
            final Commandline commandLine = new Commandline();
            commandLine.setExecutable(pythonCmd("python3"));
            commandLine.createArg().setValue(clientPath);

            final Writer writer = new OutputStreamWriter(outputStream);
            final WriterStreamConsumer systemOut = new WriterStreamConsumer(writer);
            final WriterStreamConsumer systemErr = new WriterStreamConsumer(writer);

            System.out.println("Launching Python client: " + commandLine);

            int exitCode = executeCommandLine(commandLine, systemOut, systemErr, 10);

            System.out.println("Python client's exit code: " + exitCode);
            System.out.println("Output: \n---\n" + new String(outputStream.toByteArray(), OUTPUT_FILE_ENCODING) + "---\n");
        }
        finally
        {
            outputStream.close();
        }

        return new String(outputStream.toByteArray(), OUTPUT_FILE_ENCODING);
    }

    private static String executeJar(
        final String currentDirPath,
        final String jarPath,
        final List<String> args,
        final int expectedExitCode) throws CommandLineException, IOException
    {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try
        {
            final int actualExitCode = executeJar(currentDirPath, jarPath, args, outputStream);

            assertThat("exit code", actualExitCode, is(expectedExitCode));
        }
        finally
        {
            outputStream.close();
        }

        return new String(outputStream.toByteArray(), OUTPUT_FILE_ENCODING);
    }

    private static int executeJar(
        final String currentDirPath,
        final String jarPath,
        final List<String> args,
        final ByteArrayOutputStream outputStream) throws CommandLineException, IOException
    {
        final File jarFile = new File(jarPath);
        assertThat("Jar file must exit: " + jarFile, jarFile.exists(), is(true));

        final File javaBinDir = new File(System.getProperty("java.home"), "bin");
        assertThat("Java bin dir must exit: " + javaBinDir, javaBinDir.exists(), is(true));

        final File javaExecFile = new File(javaBinDir, "java");
        assertThat("Java exec file must exit: " + javaExecFile, javaExecFile.exists(), is(true));

        final Commandline commandLine = new Commandline();
        commandLine.addEnvironment("CML_MODULES_PATH", new File(CML_MODULES_BASE_DIR).getCanonicalPath());
        commandLine.setWorkingDirectory(currentDirPath);
        commandLine.setExecutable(javaExecFile.getAbsolutePath());

        commandLine.createArg().setValue("-jar");
        commandLine.createArg().setValue(jarFile.getAbsolutePath());

        for (final String arg : args) commandLine.createArg().setValue(arg);

        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterStreamConsumer systemOut = new WriterStreamConsumer(writer);
        final WriterStreamConsumer systemErr = new WriterStreamConsumer(writer);

        System.out.println("Launching jar: " + commandLine);

        final int exitCode = executeCommandLine(commandLine, systemOut, systemErr, 10);

        System.out.println("Jar's exit code: " + exitCode);
        System.out.println("Output: \n---\n" + new String(outputStream.toByteArray(), OUTPUT_FILE_ENCODING) + "---\n");

        return exitCode;
    }

    private static String pythonCmd(String cmd)
    {
        final String pythonHomeDir = System.getenv("PYTHON_HOME");
        return pythonHomeDir + "/bin/" + cmd;
    }

    private static String getErrorModulePath(String moduleName)
    {
        return SuccessCase.CASES_DIR + File.separator + "errors" + File.separator + moduleName;
    }

    private static String getValidationModulePath(String moduleName)
    {
        return SuccessCase.CASES_DIR + File.separator + "validations" + File.separator + moduleName;
    }
}
