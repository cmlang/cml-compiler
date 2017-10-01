package templates.lang.common;

import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.foundation.TempModel;
import cml.language.foundation.TempProperty;
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
        final TempModule module = createModule();
        final TempConcept concept = TempConcept.create(module, "SomeConcept");

        to_string(concept, "empty.txt");
    }

    @Test
    public void to_string__required() throws IOException
    {
        final TempModule module = createModule();
        final TempProperty property = TempProperty.create("someProperty", NamedType.create("SomeType", null));
        final TempConcept concept = TempConcept.create(module, "SomeConcept", singletonList(property));

        to_string(concept, "required.txt");
    }

    @Test
    public void to_string__optional() throws IOException
    {
        final TempModule module = createModule();
        final TempProperty someProperty = TempProperty.create("someProperty", NamedType.create("SomeType", null));
        final TempProperty optionalProperty = TempProperty.create("optionalProperty", NamedType.create("AnotherType", "?"));
        final TempConcept concept = TempConcept.create(module, "SomeConcept", asList(someProperty, optionalProperty));

        to_string(concept, "optional.txt");
    }

    @Test
    public void to_string__sequence() throws IOException
    {
        final TempModule module = createModule();
        final TempProperty someProperty = TempProperty.create("someProperty", NamedType.create("SomeType", null));
        final TempProperty optionalProperty = TempProperty.create("optionalProperty", NamedType.create("AnotherType", "?"));
        final TempProperty sequenceProperty = TempProperty.create("sequenceProperty", NamedType.create("String", "*"));
        final TempConcept concept = TempConcept.create(module, "SomeConcept", asList(someProperty, optionalProperty, sequenceProperty));

        to_string(concept, "sequence.txt");
    }

    private void to_string(TempConcept concept, String expectedOutputFileName) throws IOException
    {
        testTemplateWithConcept("to_string", concept, expectedOutputFileName);
    }

    private static TempModule createModule()
    {
        return TempModule.create(TempModel.create(), "some_module");
    }
}
