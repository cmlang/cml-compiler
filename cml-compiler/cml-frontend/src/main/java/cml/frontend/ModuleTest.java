package cml.frontend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RunWith(Parameterized.class)
public class ModuleTest
{
    static Compiler compiler;
    static String basePath;

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> modulePaths()
    {
        final File file = new File(basePath);
        final File[] files = file.listFiles(File::isDirectory);

        return stream(files == null ? new File[0] : files)
            .map(f -> new Object[] { f.getName(), f })
            .collect(toList());
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
