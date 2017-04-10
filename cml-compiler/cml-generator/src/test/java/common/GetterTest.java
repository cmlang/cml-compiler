package common;

import cml.language.foundation.Property;
import cml.language.foundation.Type;
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
        return "common/getter";
    }

    @Test
    public void getter_call__optional() throws IOException
    {
        final String cardinality = "?"; // optional

        getter_call(cardinality, "expected.txt");
    }

    @Test
    public void getter_call__required() throws IOException
    {
        final String cardinality = null; // required

        getter_call(cardinality, "expected.txt");
    }

    @Test
    public void getter_call__set() throws IOException
    {
        final String cardinality = "*"; // set

        getter_call(cardinality, "expected.txt");
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
    public void getter_type__set() throws IOException
    {
        final String cardinality = "*"; // set

        getter_type(cardinality, "set.txt");
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
    public void interface_getter__set() throws IOException
    {
        final String cardinality = "*"; // set

        interface_getter(cardinality, "set.txt");
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
    public void class_getter__set() throws IOException
    {
        final String cardinality = "*"; // set

        class_getter(cardinality, "set.txt");
    }

    private void getter_type(String cardinality, String expectedOutput) throws IOException
    {
        for (String name : commonNameFormats)
        {
            testTemplateWithType("getter_type", Type.create(name, cardinality), expectedOutput);
        }
    }

    private void getter_call(String cardinality, String expectedOutput) throws IOException
    {
        testTemplateWithProperty("getter_call", createProperty(cardinality), expectedOutput);
    }

    private void interface_getter(String cardinality, String expectedOutput) throws IOException
    {
        testTemplateWithProperty("interface_getter", createProperty(cardinality), expectedOutput);
    }

    private void class_getter(String cardinality, String expectedOutputFileName) throws IOException
    {
        testTemplateWithProperty("class_getter", createProperty(cardinality), expectedOutputFileName);
    }

    private static Property createProperty(String cardinality)
    {
        return Property.create("SomeProperty", null, Type.create("someType", cardinality));
    }
}
