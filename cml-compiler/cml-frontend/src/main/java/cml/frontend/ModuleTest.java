package cml.frontend;

import cml.io.Console;
import com.google.common.io.Files;
import org.jooq.lambda.Seq;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static cml.frontend.Compiler.createCompiler;
import static cml.io.Console.createStringConsole;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static junit.framework.TestCase.assertEquals;
import static org.apache.commons.io.FileUtils.cleanDirectory;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jooq.lambda.Seq.seq;
import static org.junit.Assume.assumeThat;

@RunWith(Parameterized.class)
public class ModuleTest
{
    private static final Charset FILE_ENCODING = Charset.forName("UTF-8");

    private static final String SOURCE_PATH = "source";
    private static final String TARGETS_PATH = "targets";
    private static final String TESTS_PATH = "tests";
    private static final String EXPECTED_PATH = "expected";

    private static final String COMPILER_OUTPUT_TXT = "cml-compiler-output.txt";

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> modulePaths()
    {
        return subDirsOf(TESTS_PATH)
            .flatMap(moduleDir -> subDirsOf(moduleDir.getAbsolutePath()))
            .filter(moduleDir -> sourceDirOf(moduleDir).isDirectory())
            .flatMap(moduleDir -> subDirsOf(expectedPathOf(moduleDir)))
            .map(expectedDir -> new Object[] {
                testNameOf(expectedDir),
                taskNameOf(expectedDir),
                moduleDirOf(expectedDir),
                expectedDir
            })
            .collect(toList());
    }

    private static String currentTestName;
    private static String currentTaskName;

    private final String taskName;
    private final File moduleDir;
    private final File expectedDir;
    private final File targetDir;

    private String compilerOutput;

    public ModuleTest(String testName, String taskName, File moduleDir, File expectedDir)
    {
        this.taskName = taskName;
        this.moduleDir = moduleDir;
        this.expectedDir = expectedDir;
        this.targetDir = new File(moduleDir, TARGETS_PATH + "/" + taskName);

        if (!testName.equals(currentTestName) || !taskName.equals(currentTaskName))
        {
            System.out.println("\nTesting " + testName + " with task " + taskName + ":");

            currentTestName = testName;
            currentTaskName = taskName;
        }
    }

    @Before
    public void compile() throws Exception
    {
        final Console console = createStringConsole();
        final Compiler compiler = createCompiler(console);

        compiler.compile(moduleDir.getPath(), taskName);

        compilerOutput = console.toString();
    }

    @After
    public void clean() throws Exception
    {
        if (targetDir.isDirectory())
        {
            cleanDirectory(targetDir);
        }
    }

    @Test
    public void verifyCompilerOutput() throws IOException
    {
        final File expectedOutputFile = new File(expectedDir, COMPILER_OUTPUT_TXT);

        assumeThat(expectedOutputFile.isFile(), is(true));

        assertThatOutputMatches("Compiler's output", expectedOutputFile, compilerOutput);

        System.out.println("- Verified the compiler's output.");
    }

    @Test
    public void verifyTargetFiles()
    {
        assertThat(
            "Should have found all expected files, but missing: " + missingFiles().toString("/n"),
            missingFiles().count(), is(equalTo(0L)));

        verifiedFiles().forEach(this::verifyTargetFile);

        ignoredFiles().forEach(ignoredFile -> System.out.println("- Ignored target file: " + relativePathOfTargetFile(ignoredFile)));
    }

    private void verifyTargetFile(final File expectedFile)
    {
        final String relativePath = relativePathOfExpectedFile(expectedFile);
        final File targetFile = new File(targetDir, relativePath);

        assertThatOutputMatches("Target file: " + relativePath, expectedFile, targetFile);

        System.out.println("- Verified target file: " + relativePath);
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
