package templates.lang.common;

import cml.language.features.Concept;
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

        to_string(concept, "empty.txt");
    }

    @Test
    public void to_string__required() throws IOException
    {
        final Concept concept = Concept.create("SomeConcept");

        concept.addMember(Property.create("someProperty", null, Type.create("SomeType", null)));

        to_string(concept, "required.txt");
    }

    @Test
    public void to_string__optional() throws IOException
    {
        final Concept concept = Concept.create("SomeConcept");

        concept.addMember(Property.create("someProperty", null, Type.create("SomeType", null)));
        concept.addMember(Property.create("optionalProperty", null, Type.create("AnotherType", "?")));

        to_string(concept, "optional.txt");
    }

    @Test
    public void to_string__set() throws IOException
    {
        final Concept concept = Concept.create("SomeConcept");

        concept.addMember(Property.create("someProperty", null, Type.create("SomeType", null)));
        concept.addMember(Property.create("optionalProperty", null, Type.create("AnotherType", "?")));
        concept.addMember(Property.create("setProperty", null, Type.create("YetAnotherType", "*")));

        to_string(concept, "set.txt");
    }

    private void to_string(Concept concept, String expectedOutputFileName) throws IOException
    {
        testTemplateWithConcept("to_string", concept, expectedOutputFileName);
    }
}
