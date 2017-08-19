package templates.lang.common;

import cml.language.foundation.Property;
import cml.language.types.NamedType;
import org.junit.Test;

import java.io.IOException;

public class GetterTest extends LangTest
{
    public GetterTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "getter";
    }

    @Test
    public void getter_call__optional() throws IOException
    {
        final String cardinality = "?"; // optional

        getter_call(cardinality);
    }

    @Test
    public void getter_call__required() throws IOException
    {
        final String cardinality = null; // required

        getter_call(cardinality);
    }

    @Test
    public void getter_call__sequence() throws IOException
    {
        final String cardinality = "*"; // sequence

        getter_call(cardinality);
    }

    @Test
    public void getter_type__required() throws IOException
    {
        final String cardinality = null; // required

        getter_type(cardinality, "required.txt");
    }

    @Test
    public void getter_type__optional() throws IOException
    {
        final String cardinality = "?"; // optional

        getter_type(cardinality, "optional.txt");
    }

    @Test
    public void getter_type__sequence() throws IOException
    {
        final String cardinality = "*"; // sequence

        getter_type(cardinality, "sequence.txt");
    }

    @Test
    public void getter_type__sequence_integer() throws IOException
    {
        testTemplateWithType("getter_type", NamedType.create("Integer", "*"), "sequence_integer.txt");
    }

    @Test
    public void interface_getter__required() throws IOException
    {
        final String cardinality = null; // required

        interface_getter(cardinality, "required.txt");
    }

    @Test
    public void interface_getter__optional() throws IOException
    {
        final String cardinality = "?"; // optional

        interface_getter(cardinality, "optional.txt");
    }

    @Test
    public void interface_getter__sequence() throws IOException
    {
        final String cardinality = "*"; // sequence

        interface_getter(cardinality, "sequence.txt");
    }

    @Test
    public void class_getter__required() throws IOException
    {
        final String cardinality = null; // required

        class_getter(cardinality, "required.txt");
    }

    @Test
    public void class_getter__optional() throws IOException
    {
        final String cardinality = "?"; // optional

        class_getter(cardinality, "optional.txt");
    }

    @Test
    public void class_getter__sequence() throws IOException
    {
        final String cardinality = "*"; // sequence

        class_getter(cardinality, "sequence.txt");
    }

    private void getter_type(String cardinality, String expectedOutput) throws IOException
    {
        for (String name : commonNameFormats)
        {
            testTemplateWithType("getter_type", NamedType.create(name, cardinality), expectedOutput);
        }
    }

    private void getter_call(String cardinality) throws IOException
    {
        testTemplateWithProperty("getter_call", createProperty(cardinality), "expected.txt");
    }

    private void interface_getter(String cardinality, String expectedOutput) throws IOException
    {
        testTemplateWithProperty("interface_getter", createProperty(cardinality), expectedOutput);
    }

    private void class_getter(String cardinality, String expectedOutputFileName) throws IOException
    {
        testTemplateWithProperty("field_getter", createProperty(cardinality), expectedOutputFileName);
    }

    private static Property createProperty(String cardinality)
    {
        return Property.create("SomeProperty", NamedType.create("someType", cardinality));
    }
}
