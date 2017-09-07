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
import static org.hamcrest.CoreMatchers.is;
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

        System.out.println("\nTesting " + testName + ":");
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
    public void compilerOutput() throws IOException
    {
        final File expectedOutputFile = new File(expectedDir, COMPILER_OUTPUT_TXT);

        assumeThat(expectedOutputFile.isFile(), is(true));

        assertThatOutputMatches("Compiler's output", expectedOutputFile, compilerOutput);

        System.out.println("- Verified the compiler's output.");
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
        final File[] files = new File(basePath).listFiles(File::isDirectory);

        return seq(asList(files == null ? new File[0] : files));
    }

    private static void assertThatOutputMatches(
        final String reason,
        final File expectedOutputFile,
        final String actualOutput) throws IOException
    {
        final String expectedOutput = Files.toString(expectedOutputFile, FILE_ENCODING);

        assertEquals(reason, expectedOutput, actualOutput);
    }
}
