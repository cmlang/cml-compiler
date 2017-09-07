package cml.frontend;

import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

class Tester
{
    private static final int EXIT_CODE__SUCCESS = 0;
    private static final int EXIT_CODE__TESTS_FAILED = 52;

    int test()
    {
        final JUnitCore junit = new JUnitCore();

        junit.addListener(new TextListener(System.out)
        {
            @Override
            public void testStarted(final Description description) {}

            @Override
            protected void printFailure(Failure each, String prefix) {
                System.out.println(prefix + ") " + each.getTestHeader());
                System.out.println(each.getMessage());
            }
        });

        final Result result = junit.run(ModuleTest.class);

        return result.wasSuccessful() ? EXIT_CODE__SUCCESS : EXIT_CODE__TESTS_FAILED;
    }
}
