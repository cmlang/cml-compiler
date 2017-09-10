package mcml.client;

import mcml.cmlc.Concept;
import mcml.cmlc.Model;

import static java.util.Collections.emptyList;

public class Launcher
{
    public static void main(final String[] args)
    {
        final Model model = Model.createModel(null, emptyList());
        final Concept concept = Concept.createConcept( "SomeConcept", null, emptyList(), true);

        System.out.println("Mini-CML Compiler");
        System.out.println();
        System.out.println(model);
        System.out.println(concept);
    }
}
