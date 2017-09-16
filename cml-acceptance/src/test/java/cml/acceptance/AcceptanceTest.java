package cml.acceptance;

import com.google.common.io.Files;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;
import org.jooq.lambda.Seq;
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
import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertEquals;
import static org.codehaus.plexus.util.FileUtils.*;
import static org.codehaus.plexus.util.cli.CommandLineUtils.executeCommandLine;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.jooq.lambda.Seq.seq;

@RunWith(Theories.class)
public class AcceptanceTest
{
    private static final Charset OUTPUT_FILE_ENCODING = Charset.forName("UTF-8");
    private static final int PROCESS_TIMEOUT_IN_SECONDS = 180;

    private static final String BASE_DIR = "..";

    private static final String COMPILER_DIR = BASE_DIR + "/cml-compiler";
    private static final String FRONTEND_DIR = COMPILER_DIR + "/cml-frontend";
    private static final String FRONTEND_TARGET_DIR = FRONTEND_DIR + "/target";
    private static final String COMPILER_JAR = FRONTEND_TARGET_DIR + "/cml-compiler-jar-with-dependencies.jar";

    private static final String CLIENT_BASE_DIR = BASE_DIR + "/" + "cml-clients";
    private static final String CLIENT_JAR_SUFFIX = "-jar-with-dependencies.jar";

    private static final String CML_MODULES_BASE_DIR = BASE_DIR + "/" + "cml-modules";

    private static final String CML_BOOTSTRAPPING_BASE_DIR = COMPILER_DIR + "/" + "cml-bootstrapping";

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

    @DataPoints("validation-modules")
    public static String[] validationModules = {
        "abstract_property_in_abstract_concept",
        "abstract_property_redefinition",
        "association_end_property_found_in_model",
        "association_end_type_matches_property_type",
        "association_end_types_must_match",
        "association_must_have_two_association_ends",
        "compatible_generalizations",
        "conflict_redefinition",
        "generalization_compatible_redefinition",
        "not_own_generalization",
        "property_type_specified_or_inferred",
        "property_type_assignable_from_expression_type",
        "unique_property_name"
    };

    @DataPoints("failing-modules")
    public static String[] failingModules = {
        "invalid_module_name",
        "missing_ancestor",
        "parsing_failed"
    };

    @BeforeClass
    public static void buildCompiler() throws MavenInvocationException
    {
        buildMavenModule(COMPILER_DIR);

        final File targetDir = new File(FRONTEND_TARGET_DIR);
        assertThat("Target dir must exist: " + targetDir, targetDir.exists(), is(true));
    }

    @Test
    public void cml_modules()
    {
        moduleDirs(CML_MODULES_BASE_DIR).forEach(this::testModule);
    }

    @Test
    public void cml_bootstrapping_modules()
    {
        moduleDirs(CML_BOOTSTRAPPING_BASE_DIR).forEach(this::testModule);
    }

    private Seq<File> moduleDirs(String baseDir)
    {
        final File[] subDirs = new File(baseDir).listFiles(File::isDirectory);

        return seq(asList(subDirs == null ? new File[0] : subDirs)).filter(AcceptanceTest::isModuleDir);
    }

    private static boolean isModuleDir(File subDir)
    {
        return new File(subDir, "source").isDirectory() || new File(subDir, "templates").isDirectory();
    }

    private void testModule(File moduleDir)
    {
        try
        {
            System.out.println("--------");
            System.out.println("Testing module: " + moduleDir.getName());

            executeJar(moduleDir.getCanonicalPath(), COMPILER_JAR, singletonList("test"), SUCCESS, System.out);
        }
        catch (IOException exception)
        {
            throw new RuntimeException("IOException: " + exception.getMessage(), exception);
        }
        catch (CommandLineException exception)
        {
            throw new RuntimeException("CommandLineException: " + exception.getMessage(), exception);
        }
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

    private static void buildMavenModule(final String baseDir) throws MavenInvocationException
    {
        System.out.println("Building: " + baseDir);

        System.setProperty("maven.home", System.getenv("M2_HOME"));

        final InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(new File(baseDir));
        request.setGoals(singletonList("install"));
        request.setInteractive(false);

        final Invoker invoker = new DefaultInvoker();
        final InvocationResult result = invoker.execute(request);

        if (result.getExitCode() != 0) throw new MavenInvocationException("Exit code: " + result.getExitCode());
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
            executeJar(currentDirPath, jarPath, args, expectedExitCode, outputStream);
        }
        catch (AssertionError error)
        {
            System.out.println("Failure in output: \n" + new String(outputStream.toByteArray(), OUTPUT_FILE_ENCODING));

            throw error;
        }
        finally
        {
            outputStream.close();
        }

        return new String(outputStream.toByteArray(), OUTPUT_FILE_ENCODING);
    }

    private static void executeJar(
            final String currentDirPath,
            final String jarPath,
            final List<String> args,
            final int expectedExitCode,
            final OutputStream outputStream) throws CommandLineException, IOException
    {
        final int actualExitCode = executeJar(currentDirPath, jarPath, args, outputStream);

        assertThat("exit code", actualExitCode, is(expectedExitCode));
    }

    private static int executeJar(
        final String currentDirPath,
        final String jarPath,
        final List<String> args,
        final OutputStream outputStream) throws CommandLineException, IOException
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

        final int exitCode = executeCommandLine(commandLine, systemOut, systemErr, PROCESS_TIMEOUT_IN_SECONDS);

        System.out.println("Jar's exit code: " + exitCode);

        return exitCode;
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
