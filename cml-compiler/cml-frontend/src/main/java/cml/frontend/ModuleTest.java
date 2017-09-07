package cml.frontend;

import cml.io.Console;
import com.google.common.io.Files;
import org.apache.maven.shared.invoker.*;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.WriterStreamConsumer;
import org.hamcrest.core.Is;
import org.jooq.lambda.Seq;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import static cml.frontend.Compiler.createCompiler;
import static cml.io.Console.createStringConsole;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static junit.framework.TestCase.assertEquals;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.codehaus.plexus.util.cli.CommandLineUtils.executeCommandLine;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jooq.lambda.Seq.seq;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ModuleTest
{
    private static final Charset FILE_ENCODING = Charset.forName("UTF-8");
    private static final int PROCESS_TIMEOUT_IN_SECONDS = 60;

    private static final String SOURCE_PATH = "source";
    private static final String TARGETS_PATH = "targets";
    private static final String TESTS_PATH = "tests";
    private static final String EXPECTED_PATH = "expected";

    private static final String COMPILER_OUTPUT_TXT = "cml-compiler-output.txt";
    private static final String CLIENT_OUTPUT_TXT = "expected-client-output.txt";
    private static final String CLIENT_JAR_SUFFIX = "-jar-with-dependencies.jar";

    static String selectedTestName;
    static String selectedTaskName;

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> testProperties()
    {
        return expectedDirs()
            .map(expectedDir -> new Object[] {
                testNameOf(expectedDir),
                taskNameOf(expectedDir),
                moduleDirOf(expectedDir),
                expectedDir
            })
            .filter(properties -> isSelectedTestModule(properties[0]))
            .filter(properties -> isSelectedTask(properties[1]))
            .collect(toList());
    }

    static boolean isSelectedTestModule(final Object property)
    {
        return selectedTestName == null || property.equals(selectedTestName) || ((String) property).startsWith(selectedTestName + "/");
    }

    static boolean isSelectedTask(final Object property)
    {
        return selectedTaskName == null || property.equals(selectedTaskName);
    }

    static Seq<String> selectedTestNames()
    {
        return expectedDirs().map(dir -> testNameOf(dir)).filter(testName -> isSelectedTestModule(testName));
    }

    static Seq<String> selectedTaskNames()
    {
        return expectedDirs().map(dir -> taskNameOf(dir)).filter(testName -> isSelectedTestModule(testName));
    }

    static Seq<File> expectedDirs()
    {
        return testModules().flatMap(moduleDir -> subDirsOf(expectedPathOf(moduleDir)));
    }

    static Seq<File> testModules()
    {
        return subDirsOf(TESTS_PATH)
            .flatMap(moduleDir -> subDirsOf(moduleDir.getAbsolutePath()))
            .filter(moduleDir -> sourceDirOf(moduleDir).isDirectory());
    }

    private final String testName;
    private final String taskName;
    private final File moduleDir;
    private final File expectedDir;
    private final File targetDir;

    private String compilerOutput;

    public ModuleTest(String testName, String taskName, File moduleDir, File expectedDir)
    {
        this.testName = testName;
        this.taskName = taskName;
        this.moduleDir = moduleDir;
        this.expectedDir = expectedDir;
        this.targetDir = new File(moduleDir, TARGETS_PATH + "/" + taskName);
    }

    @Before
    public void compileTestModule() throws Exception
    {
        final Console console = createStringConsole();
        final Compiler compiler = createCompiler(console);

        compiler.compile(moduleDir.getPath(), taskName);

        compilerOutput = console.toString();
    }

    @After
    public void cleanTargetDir() throws Exception
    {
        if (targetDir.isDirectory())
        {
            cleanDirectory(targetDir);
        }
    }

    @Test
    public void verifyTestModule() throws IOException
    {
        System.out.println("\n\nTesting " + testName + " with task " + taskName + ":");

        // Compiling test module:
        verifyCompilerOutput();
        verifyTargetFiles();

        // Building generated target:
        buildMavenModule();
        checkPythonTypes();
        installPythonPackage();

        // Executing clients:
        executeJavaClient();
        executePythonClient();
    }

    private void verifyCompilerOutput() throws IOException
    {
        final File expectedOutputFile = new File(expectedDir, COMPILER_OUTPUT_TXT);

        if (expectedOutputFile.isFile())
        {
            System.out.print("- Verifying the compiler's output ...");

            assertThatOutputMatches("Compiler's output", expectedOutputFile, compilerOutput);

            System.out.println();
        }
        else
        {
            System.out.println("- Ignored the compiler's output.");
        }
    }

    private void verifyTargetFiles()
    {
        assertThat(
            "Should have found all expected files, but missing: " + missingFiles().toString("/n"),
            missingFiles().count(), is(equalTo(0L)));

        verifiedFiles().forEach(this::verifyTargetFile);

        ignoredFiles().forEach(ignoredFile -> System.out.println("- Ignored target file: " + relativePathOfTargetFile(ignoredFile)));
    }

    private void buildMavenModule()
    {
        if (isMavenModule())
        {
            System.out.print("- Building test Maven module: " + targetDir.getName() + " ");

            buildMavenModule(targetDir);

            System.out.println();
        }
    }

    private void checkPythonTypes()
    {
        if (isPythonModule())
        {
            subDirsOf(targetDir.getAbsolutePath()).forEach(ModuleTest::checkPythonTypes);
        }
    }

    private void installPythonPackage()
    {
        if (isPythonModule())
        {
            installPythonPackage(targetDir);
        }
    }

    private void executeJavaClient()
    {
        final String clientPath = "clients/" + taskName;
        final File javaClient = new File(moduleDir, clientPath);

        if (new File(javaClient, "pom.xml").isFile())
        {
            System.out.print("- Running Java client: " + clientPath + " ");

            executeJavaClient(javaClient);
        }
    }

    private void executePythonClient()
    {
        final String clientPath = "clients/" + taskName + "/client.py";
        final File pythonClient = new File(moduleDir, clientPath);

        if (pythonClient.isFile())
        {
            System.out.print("- Running Python client: " + clientPath + " ");

            executePythonClient(pythonClient);
        }
    }

    private void verifyTargetFile(final File expectedFile)
    {
        final String relativePath = relativePathOfExpectedFile(expectedFile);
        final File targetFile = new File(targetDir, relativePath);

        assertThatOutputMatches("Target file: " + relativePath, expectedFile, targetFile);

        System.out.println("- Verified target file: " + relativePath);
    }

    private boolean isPythonModule()
    {
        return new File(targetDir, "setup.py").isFile();
    }

    private boolean isMavenModule()
    {
        return new File(targetDir, "pom.xml").isFile();
    }

    private String relativePathOfExpectedFile(File expectedFile)
    {
        return expectedFile.getAbsolutePath().replace(expectedDir.getAbsolutePath() + "/", "");
    }

    private String relativePathOfTargetFile(File targetFile)
    {
        return targetFile.getAbsolutePath().replace(targetDir.getAbsolutePath() + "/", "");
    }

    private Seq<File> missingFiles()
    {
        return expectedFiles().filter(file -> !targetContainsExpectedFile(file));
    }

    private Seq<File> verifiedFiles()
    {
        return expectedFiles().filter(this::targetContainsExpectedFile);
    }

    private Seq<File> ignoredFiles()
    {
        return targetFiles().filter(file -> !expectedContainsTargetFile(file));
    }

    private Seq<File> expectedFiles()
    {
        return filesOf(expectedDir.getAbsolutePath()).filter(file -> !file.getName().equals(COMPILER_OUTPUT_TXT));
    }

    private Seq<File> targetFiles()
    {
        return filesOf(targetDir.getAbsolutePath());
    }

    private boolean targetContainsExpectedFile(File expectedFile)
    {
        final File targetFile = fromExpectedToTargetFile(expectedFile);

        return targetFiles().contains(targetFile);
    }

    private boolean expectedContainsTargetFile(File targetFile)
    {
        final File expectedFile = fromTargetToExpectedFile(targetFile);

        return expectedFiles().contains(expectedFile);
    }

    private File fromExpectedToTargetFile(File expectedFile)
    {
        final String expectedFilePath = expectedFile.getAbsolutePath();
        final String expectedDirPath = expectedDir.getAbsolutePath();
        final String targetDirPath = targetDir.getAbsolutePath();

        return new File(expectedFilePath.replace(expectedDirPath, targetDirPath));
    }

    private File fromTargetToExpectedFile(File targetFile)
    {
        final String targetFilePath = targetFile.getAbsolutePath();
        final String targetDirPath = targetDir.getAbsolutePath();
        final String expectedDirPath = expectedDir.getAbsolutePath();

        return new File(targetFilePath.replace(targetDirPath, expectedDirPath));
    }

    private static String testNameOf(final File expectedDir)
    {
        final File moduleDir = moduleDirOf(expectedDir);
        return moduleDir.getParentFile().getName() + "/" + moduleDir.getName();
    }

    private static String taskNameOf(final File expectedDir)
    {
        return expectedDir.getName();
    }

    private static File moduleDirOf(final File expectedDir)
    {
        return expectedDir.getParentFile().getParentFile();
    }

    private static File sourceDirOf(final File moduleDir)
    {
        return new File(moduleDir, SOURCE_PATH);
    }

    private static String expectedPathOf(final File moduleDir)
    {
        return moduleDir.getAbsolutePath() + "/" + EXPECTED_PATH;
    }

    private static Seq<File> subDirsOf(String basePath)
    {
        final File[] subDirs = new File(basePath).listFiles(File::isDirectory);
        final List<File> subDirList = asList(subDirs == null ? new File[0] : subDirs);

        return seq(subDirList);
    }

    private static Seq<File> filesOf(String basePath)
    {
        final File[] files = new File(basePath).listFiles(File::isFile);
        final List<File> fileList = asList(files == null ? new File[0] : files);

        final Seq<File> subFiles = subDirsOf(basePath).flatMap(subDir -> filesOf(subDir.getAbsolutePath()));

        return seq(fileList).concat(subFiles);
    }

    private static void executeJavaClient(File clientModuleDir)
    {
        buildMavenModule(clientModuleDir);

        final File clientTargetDir = new File(clientModuleDir, "target");
        assertThat("Client target dir must exist: " + clientTargetDir, clientTargetDir.exists(), Is.is(true));

        final Optional<File> clientJarPath = filesOf(clientTargetDir.getAbsolutePath()).filter(file -> file.getAbsolutePath().endsWith(CLIENT_JAR_SUFFIX)).findSingle();

        assertTrue("Client jar should have been found at: " + clientTargetDir, clientJarPath.isPresent());

        executeJar(clientTargetDir, clientJarPath.get().getAbsolutePath());
    }

    private static void executeJar(final File clientTargetDir, final String jarPath)
    {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            try
            {
                final int exitCode = executeJar(clientTargetDir.getPath(), jarPath, emptyList(), outputStream);

                final String actualClientOutput = stringOf(outputStream);
                final File expectedOutputFile = new File(clientTargetDir.getParentFile(), CLIENT_OUTPUT_TXT);

                if (expectedOutputFile.isFile())
                {
                    assertThatOutputMatches("Client's output", expectedOutputFile, actualClientOutput);
                }
                else
                {
                    System.out.println("- Ignored the client's output.");
                }

                assertThat(
                    "Client's exit code: " + exitCode + " - output:\n---\n" + actualClientOutput + "\n---",
                    exitCode, is(0));
            }
            catch (CommandLineException exception)
            {
                System.out.println();
                System.out.println("--------");
                System.out.println("Error running client: " + clientTargetDir.getName());
                System.out.println(stringOf(outputStream));

                throw new RuntimeException("CommandLineException: " + exception.getMessage(), exception);
            }
        }
        catch (IOException exception)
        {
            System.out.println();

            throw new RuntimeException("IOException: " + exception.getMessage(), exception);
        }
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
        commandLine.setWorkingDirectory(currentDirPath);
        commandLine.setExecutable(javaExecFile.getAbsolutePath());

        commandLine.createArg().setValue("-jar");
        commandLine.createArg().setValue(jarFile.getAbsolutePath());

        for (final String arg : args) commandLine.createArg().setValue(arg);

        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterStreamConsumer systemOut = new WriterStreamConsumer(writer);
        final WriterStreamConsumer systemErr = new WriterStreamConsumer(writer);

        return executeCommandLine(commandLine, systemOut, systemErr, PROCESS_TIMEOUT_IN_SECONDS);
    }

    private static void buildMavenModule(final File moduleDir)
    {
        final String m2_home = System.getenv("M2_HOME");

        assertThat(
            "In order to build the generated module, Maven should be installed and M2_HOME set.",
            new File(m2_home).isDirectory());

        System.setProperty("maven.home", m2_home);

        final InvocationRequest request = new DefaultInvocationRequest();
        request.setBaseDirectory(moduleDir);
        request.setGoals(asList("clean", "install"));
        request.setInteractive(false);

        final Console console = createStringConsole();
        final Invoker invoker = new DefaultInvoker();
        invoker.setOutputHandler(line -> {
            System.out.print(".");
            console.println(line);
        });

        try
        {
            final InvocationResult result = invoker.execute(request);

            assertThat(
                "Maven failure - exit code: " + result.getExitCode() + "\n" + console.toString(),
                result.getExitCode(), is(equalTo(0)));
        }
        catch (MavenInvocationException exception)
        {
            System.out.println();
            System.out.println("--------");
            System.out.println("Error running Maven:");
            System.out.println(console.toString());

            throw new RuntimeException("MavenInvocationException: " + exception.getMessage(), exception);
        }
    }

    private static void checkPythonTypes(final File moduleDir)
    {
        System.out.println("- Checking types of Python module: " + moduleDir.getName());

        assertThat(
            "In order to check types of the generated module, Python 3 should be installed and PYTHON_HOME set.",
            new File(pythonCmd("python3")).isFile());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            // https://github.com/python/mypy
            final Commandline commandLine = new Commandline();
            commandLine.setExecutable(pythonCmd("python3"));
            commandLine.createArg().setValue("-m");
            commandLine.createArg().setValue("mypy");
            commandLine.createArg().setValue(moduleDir.getCanonicalPath());

            final Writer writer = new OutputStreamWriter(outputStream);
            final WriterStreamConsumer systemOut = new WriterStreamConsumer(writer);
            final WriterStreamConsumer systemErr = new WriterStreamConsumer(writer);

            try
            {
                final int exitCode = executeCommandLine(commandLine, systemOut, systemErr, PROCESS_TIMEOUT_IN_SECONDS);

                assertThat(
                    "mypy's exit code: " + exitCode + "\nmypy's output: \n---\n" + stringOf(outputStream) + "---\n",
                    exitCode, Is.is(0));
            }
            catch (CommandLineException exception)
            {
                System.out.println("--------");
                System.out.println("Error running mypy:");
                System.out.println(stringOf(outputStream));

                throw new RuntimeException("CommandLineException: " + exception.getMessage(), exception);
            }
        }
        catch (IOException exception)
        {
            throw new RuntimeException("IOException: " + exception.getMessage(), exception);
        }
    }

    private static void installPythonPackage(final File packageDir)
    {
        System.out.println("- Installing Python package: " + packageDir.getName());

        assertThat(
            "In order to install the generated Python package, pip should be installed.",
            new File(pythonCmd("pip3")).isFile());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            // --upgrade: overwriting previous installation - https://packaging.python.org/installing
            final Commandline commandLine = new Commandline();
            commandLine.setExecutable(pythonCmd("pip3"));
            commandLine.createArg().setValue("install");
            commandLine.createArg().setValue("--upgrade");
            commandLine.createArg().setValue(packageDir.getCanonicalPath());

            final Writer writer = new OutputStreamWriter(outputStream);
            final WriterStreamConsumer systemOut = new WriterStreamConsumer(writer);
            final WriterStreamConsumer systemErr = new WriterStreamConsumer(writer);

            try
            {
                final int exitCode = executeCommandLine(commandLine, systemOut, systemErr, PROCESS_TIMEOUT_IN_SECONDS);

                assertThat(
                    "pip's exit code: " + exitCode + "\npip's output: \n---\n" + stringOf(outputStream) + "---\n",
                    exitCode, is(0));
            }
            catch (CommandLineException exception)
            {
                System.out.println("--------");
                System.out.println("Error running pip:");
                System.out.println(stringOf(outputStream));

                throw new RuntimeException("CommandLineException: " + exception.getMessage(), exception);
            }
        }
        catch (IOException exception)
        {
            throw new RuntimeException("IOException: " + exception.getMessage(), exception);
        }
    }

    private static void executePythonClient(File client)
    {
        assertThat(
            "In order to run the Python client, Python 3 should be installed and PYTHON_HOME set.",
            new File(pythonCmd("python3")).isFile());

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
        {
            // Executing Python module - https://www.python.org/dev/peps/pep-0338/#current-behaviour
            final Commandline commandLine = new Commandline();
            commandLine.setExecutable(pythonCmd("python3"));
            commandLine.createArg().setValue(client.getCanonicalPath());

            final Writer writer = new OutputStreamWriter(outputStream);
            final WriterStreamConsumer systemOut = new WriterStreamConsumer(writer);
            final WriterStreamConsumer systemErr = new WriterStreamConsumer(writer);

            try
            {
                int exitCode = executeCommandLine(commandLine, systemOut, systemErr, PROCESS_TIMEOUT_IN_SECONDS);

                final String actualClientOutput = stringOf(outputStream);
                final File expectedOutputFile = new File(client.getParentFile(), CLIENT_OUTPUT_TXT);

                if (expectedOutputFile.isFile())
                {
                    assertThatOutputMatches("Client's output", expectedOutputFile, actualClientOutput);
                }
                else
                {
                    System.out.println("\n- Ignored the client's output.");
                }

                assertThat(
                    "Client's exit code: " + exitCode + "\noutput: \n---\n" + actualClientOutput + "---\n",
                    exitCode, is(0));
            }
            catch (CommandLineException exception)
            {
                System.out.println("--------");
                System.out.println("Error running client:");
                System.out.println(stringOf(outputStream));

                throw new RuntimeException("CommandLineException: " + exception.getMessage(), exception);
            }
        }
        catch (IOException exception)
        {
            throw new RuntimeException("IOException: " + exception.getMessage(), exception);
        }
    }

    private static String stringOf(final ByteArrayOutputStream outputStream)
    {
        return new String(outputStream.toByteArray(), FILE_ENCODING);
    }

    private static String pythonCmd(String cmd)
    {
        final String pythonHomeDir = System.getenv("PYTHON_HOME");
        return pythonHomeDir + "/bin/" + cmd;
    }

    private static void assertThatOutputMatches(
        final String reason,
        final File expectedOutputFile,
        final String actualOutput) throws IOException
    {
        final String expectedOutput = Files.toString(expectedOutputFile, FILE_ENCODING);

        assertEquals(reason, expectedOutput, actualOutput);
    }

    private static void assertThatOutputMatches(
        final String reason,
        final File expectedOutputFile,
        final File actualOutputFile)
    {
        try
        {
            final String expectedOutput = Files.toString(expectedOutputFile, FILE_ENCODING);
            final String actualOutput = Files.toString(actualOutputFile, FILE_ENCODING);

            assertEquals(reason, expectedOutput, actualOutput);
        }
        catch (IOException exception)
        {
            throw new AssertionError("IOException: " + exception.getMessage());
        }
    }
}
