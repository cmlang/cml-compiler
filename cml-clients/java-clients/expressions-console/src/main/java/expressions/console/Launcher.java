package expressions.console;

import expressions.cases.ExpressionCases;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Expressions Console");
        System.out.println();

        final ExpressionCases cases = new ExpressionCases();
        System.out.println("LiteralStringInit = " + cases.getLiteralStringInit());
        System.out.println("LiteralIntegerInit = " + cases.getLiteralIntegerInit());
        System.out.println("LiteralDecimalInit = " + cases.getLiteralDecimalInit());
    }
}
