package templates.lang.common;

import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.Model;
import cml.language.foundation.Property;
import cml.language.types.NamedType;
import org.junit.Test;

import java.io.IOException;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

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
        final Module module = createModule();
        final Concept concept = Concept.create(module, "SomeConcept");

        to_string(concept, "empty.txt");
    }

    @Test
    public void to_string__required() throws IOException
    {
        final Module module = createModule();
        final Property property = Property.create("someProperty", NamedType.create("SomeType", null));
        final Concept concept = Concept.create(module, "SomeConcept", singletonList(property));

        to_string(concept, "required.txt");
    }

    @Test
    public void to_string__optional() throws IOException
    {
        final Module module = createModule();
        final Property someProperty = Property.create("someProperty", NamedType.create("SomeType", null));
        final Property optionalProperty = Property.create("optionalProperty", NamedType.create("AnotherType", "?"));
        final Concept concept = Concept.create(module, "SomeConcept", asList(someProperty, optionalProperty));

        to_string(concept, "optional.txt");
    }

    @Test
    public void to_string__sequence() throws IOException
    {
        final Module module = createModule();
        final Property someProperty = Property.create("someProperty", NamedType.create("SomeType", null));
        final Property optionalProperty = Property.create("optionalProperty", NamedType.create("AnotherType", "?"));
        final Property sequenceProperty = Property.create("sequenceProperty", NamedType.create("String", "*"));
        final Concept concept = Concept.create(module, "SomeConcept", asList(someProperty, optionalProperty, sequenceProperty));

        to_string(concept, "sequence.txt");
    }

    private void to_string(Concept concept, String expectedOutputFileName) throws IOException
    {
        testTemplateWithConcept("to_string", concept, expectedOutputFileName);
    }

    private static Module createModule()
    {
        return Module.create(Model.create(), "some_module");
    }
}
