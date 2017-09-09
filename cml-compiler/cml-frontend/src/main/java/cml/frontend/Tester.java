package cml.frontend;

import org.jetbrains.annotations.Nullable;
import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

class Tester
{
    private static final int EXIT_CODE__SUCCESS = 0;
    private static final int EXIT_CODE__TESTS_FAILED = 52;

    int test(final @Nullable String testName, final @Nullable String taskName)
    {
        final JUnitCore junit = new JUnitCore();

        junit.addListener(new TextListener(System.out)
        {
            @Override
            public void testStarted(final Description description) {}

            @Override
            protected void printHeader(final long runTime)
            {
                System.out.println();

                super.printHeader(runTime);
            }

            @Override
            protected void printFailures(final Result result)
            {
                System.out.println();
                super.printFailures(result);
            }

            @Override
            protected void printFailure(Failure failure, String prefix) {
                System.out.println();
                System.out.println(prefix + ") " + headerOf(failure));
                System.out.println();
                System.out.println(failure.getMessage());
            }

            private String headerOf(final Failure failure)
            {
                final String header = failure.getTestHeader();
                final int start = "verifyTestModule".length() + 1;
                final int end = header.indexOf(ModuleTest.class.getName()) - 2;
                return header.substring(start, end);
            }
        });

        ModuleTest.selectedTestName = testName;
        ModuleTest.selectedTaskName = taskName;

        final Result result = junit.run(ModuleTest.class);

        if (result.getRunCount() == 0)
        {
            if (testName != null && ModuleTest.selectedTestNames().count() == 0)
            {
                System.out.println("Test module not found: " + testName);
            }

            if (taskName != null && ModuleTest.selectedTaskNames().count() == 0)
            {
                System.out.println("Task not found: " + taskName);
            }
        }

        return testsExecuted(result, testName, taskName) && result.wasSuccessful() ? EXIT_CODE__SUCCESS : EXIT_CODE__TESTS_FAILED;
    }

    private boolean testsExecuted(final Result result, String testName, String taskName)
    {
        return (testName == null && taskName == null) || result.getRunCount() > 0;
    }
}
