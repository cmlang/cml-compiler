package templates.lang.common;

import cml.language.Model;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import org.junit.Test;

import java.io.IOException;

public class ToStringTest  extends LangTest
{
    public ToStringTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "to_string";
    }

    @Test
    public void to_string__empty() throws IOException
    {
        final Concept concept = Concept.create("SomeConcept");

        final Module module = createModule();
        module.addMember(concept);

        to_string(concept, "empty.txt");
    }

    @Test
    public void to_string__required() throws IOException
    {
        final Concept concept = Concept.create("SomeConcept");

        concept.addMember(Property.create("someProperty", Type.create("SomeType", null)));

        final Module module = createModule();
        module.addMember(concept);

        to_string(concept, "required.txt");
    }

    @Test
    public void to_string__optional() throws IOException
    {
        final Concept concept = Concept.create("SomeConcept");

        concept.addMember(Property.create("someProperty", Type.create("SomeType", null)));
        concept.addMember(Property.create("optionalProperty", Type.create("AnotherType", "?")));

        final Module module = createModule();
        module.addMember(concept);

        to_string(concept, "optional.txt");
    }

    @Test
    public void to_string__sequence() throws IOException
    {
        final Concept concept = Concept.create("SomeConcept");

        concept.addMember(Property.create("someProperty", Type.create("SomeType", null)));
        concept.addMember(Property.create("optionalProperty", Type.create("AnotherType", "?")));
        concept.addMember(Property.create("sequenceProperty", Type.create("String", "*")));

        final Module module = createModule();
        module.addMember(concept);

        to_string(concept, "sequence.txt");
    }

    private void to_string(Concept concept, String expectedOutputFileName) throws IOException
    {
        testTemplateWithConcept("to_string", concept, expectedOutputFileName);
    }

    private static Module createModule()
    {
        final Model model = Model.create();
        final Module module = Module.create("some_module");
        model.addMember(module);
        return module;
    }
}
