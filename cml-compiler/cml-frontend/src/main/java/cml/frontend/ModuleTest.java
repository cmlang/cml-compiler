package cml.frontend;

import org.jooq.lambda.Seq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;

@RunWith(Parameterized.class)
public class ModuleTest
{
    static Compiler compiler;

    private static final String basePath = "tests";

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> modulePaths()
    {
        return subDirsOf(new File(basePath))
            .flatMap(dir -> subDirsOf(dir))
            .map(f -> new Object[] { f.getName(), f })
            .collect(toList());
    }

    private static Seq<File> subDirsOf(File baseDir)
    {
        final File[] files = baseDir.listFiles(File::isDirectory);

        return seq(asList(files == null ? new File[0] : files));
    }

    private final String moduleName;
    private final File modulePath;

    public ModuleTest(String moduleName, File modulePath)
    {
        this.moduleName = moduleName;
        this.modulePath = modulePath;
    }

    @Test
    public void testModule()
    {
        System.out.println("Testing: " + moduleName);

        compiler.compile(modulePath.getPath(), "result");
    }
}
