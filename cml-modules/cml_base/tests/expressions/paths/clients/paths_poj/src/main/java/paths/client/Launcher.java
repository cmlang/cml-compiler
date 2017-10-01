package paths.client;

import paths.poj.*;

import java.math.BigDecimal;

import static java.util.Arrays.asList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Paths Client (poj)");
        System.out.println();

        final AnotherConcept anotherConcept = new AnotherConcept(BigDecimal.TEN);
        final AnotherConcept anotherConcept2 = new AnotherConcept(BigDecimal.TEN.add(BigDecimal.TEN));
        final SomeConcept someConcept = new SomeConcept(-1, asList(anotherConcept, anotherConcept2), anotherConcept);
        final SomeConcept someConcept2 = new SomeConcept(2, asList(anotherConcept2, anotherConcept), anotherConcept2);
        final ExpressionCases cases = new ExpressionCases("foo", someConcept, asList(someConcept, someConcept2));
        
        System.out.println("self_var = " + cases.getSelfVar());
        System.out.println("single_var = " + cases.getSingleVar());
        System.out.println();

        System.out.println("path_var = " + cases.getPathVar());
        System.out.println("path_var2 = " + cases.getPathVar2());
        System.out.println();

        System.out.println("path_var3 = " + cases.getPathVar3());
        System.out.println("path_bars = " + cases.getPathBars());
        System.out.println("path_foos = " + cases.getPathFoos());
    }
}
