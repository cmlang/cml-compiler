package expressions.console;

import expressions.cases.AnotherConcept;
import expressions.cases.ExpressionCases;
import expressions.cases.SomeConcept;

import java.math.BigDecimal;

import static java.util.Arrays.asList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Expressions Console");
        System.out.println();

        final AnotherConcept anotherConcept = new AnotherConcept(BigDecimal.TEN);
        final AnotherConcept anotherConcept2 = new AnotherConcept(BigDecimal.TEN.add(BigDecimal.TEN));
        final SomeConcept someConcept = new SomeConcept(-1, anotherConcept);
        final SomeConcept someConcept2 = new SomeConcept(2, anotherConcept2);
        final ExpressionCases cases = new ExpressionCases("foo", someConcept, asList(someConcept, someConcept2));
        
        System.out.println("LiteralStringInit = " + cases.getLiteralStringInit());
        System.out.println("LiteralIntegerInit = " + cases.getLiteralIntegerInit());
        System.out.println("LiteralDecimalInit = " + cases.getLiteralDecimalInit());
        System.out.println();

        System.out.println("self_var = " + cases.getSelfVar());
        System.out.println("single_var = " + cases.getSingleVar());
        System.out.println();

        System.out.println("path_var = " + cases.getPathVar());
        System.out.println("path_var2 = " + cases.getPathVar2());
        System.out.println();

        System.out.println("path_var3 = " + cases.getPathVar3());
        System.out.println("path_bars = " + cases.getPathBars());
    }
}
