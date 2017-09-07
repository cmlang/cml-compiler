package cml.frontend;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

class Tester
{
    private static final int EXIT_CODE__SUCCESS = 0;
    private static final int EXIT_CODE__TESTS_FAILED = 52;

    private final Compiler compiler;

    Tester(final Compiler compiler)
    {
        this.compiler = compiler;
    }

    int test()
    {
        ModuleTest.compiler = compiler;

        final JUnitCore junit = new JUnitCore();

        junit.addListener(new TextListener(System.out));

        final Result result = junit.run(ModuleTest.class);

        return result.wasSuccessful() ? EXIT_CODE__SUCCESS : EXIT_CODE__TESTS_FAILED;
    }
}
