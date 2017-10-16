package paths.client;

import paths.poj.*;

import static java.util.Arrays.asList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Paths Client (poj)");
        System.out.println();

        final AnotherConcept anotherConcept = new AnotherConcept(new Etc());
        final AnotherConcept anotherConcept2 = new AnotherConcept(new Etc());
        final SomeConcept someConcept = new SomeConcept(-1, new Bar(), asList(anotherConcept, anotherConcept2), anotherConcept);
        final SomeConcept someConcept2 = new SomeConcept(2, new Bar(), asList(anotherConcept2, anotherConcept), anotherConcept2);
        final ExpressionCases cases = new ExpressionCases("foo", someConcept, asList(someConcept2, someConcept), null);
        
        System.out.println("self_var = " + cases.getSelfVar());
        System.out.println("single_var = " + cases.getSingleVar());
        System.out.println();

        System.out.println("path_var = " + cases.getPathVar());
        System.out.println("path_var2 = " + cases.getPathVar2());
        System.out.println();

        System.out.println("path_var3 = " + cases.getPathVar3());
        System.out.println("path_bars = " + cases.getPathBars());
        System.out.println("path_foos = " + cases.getPathFoos());
        System.out.println();

        System.out.println("somePathList = " + cases.getSomePathList());
        System.out.println("sorted_list = " + cases.getSortedList());
    }
}
