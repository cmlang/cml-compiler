package mcml.compiler;

import mcml.language.Concept;
import mcml.language.Model;

import static java.util.Collections.emptySet;

public class Launcher
{
    public static void main(final String[] args)
    {
        final Model model = Model.createModel(null, emptySet());
        final Concept concept = Concept.createConcept(true, "SomeConcept", null, emptySet());

        System.out.println("Mini-CML Compiler");
        System.out.println();
        System.out.println(model);
        System.out.println(concept);
    }
}
