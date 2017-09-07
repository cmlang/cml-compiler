package expressions.console;

import literals.Expressions;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Literal Expressions");
        System.out.println();

        final Expressions expressions = new Expressions();
        
        System.out.println("LiteralTrueBoolean = " + expressions.getLiteralTrueBoolean());
        System.out.println("LiteralFalseBoolean = " + expressions.getLiteralFalseBoolean());
        System.out.println("LiteralStringInit = " + expressions.getLiteralStringInit());
        System.out.println("LiteralIntegerInit = " + expressions.getLiteralIntegerInit());
        System.out.println("LiteralDecimalInit = " + expressions.getLiteralDecimalInit());
        System.out.println("LiteralDecimalInit2 = " + expressions.getLiteralDecimalInit2());
    }
}
