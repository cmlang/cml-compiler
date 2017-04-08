package mcml.compiler;

import mcml.language.Concept;
import mcml.language.Model;

import static java.util.Collections.emptySet;

public class Launcher
{
    public static void main(final String[] args)
    {
        final Model model = Model.create(null, emptySet());
        final Concept concept = Concept.create(null, emptySet(), "SomeConcept", true);

        System.out.println("Mini-CML Compiler");
        System.out.println();
        System.out.println(model);
        System.out.println(concept);
    }
}
