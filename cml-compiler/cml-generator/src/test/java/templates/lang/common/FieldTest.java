package templates.lang.common;

import cml.language.generated.Property;
import cml.language.types.TempNamedType;
import org.junit.Test;

import java.io.IOException;

import static java.util.Collections.emptyList;

public class FieldTest extends LangTest
{
    public FieldTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "field";
    }

    @Test
    public void field_type__optional() throws IOException
    {
        final String cardinality = "?"; // optional

        field_type(cardinality, "optional.txt");
    }

    @Test
    public void field_type__required() throws IOException
    {
        final String cardinality = null; // required

        field_type(cardinality, "required.txt");
    }

    @Test
    public void field_type__sequence() throws IOException
    {
        final String cardinality = "*"; // sequence

        field_type(cardinality, "sequence.txt");
    }

    private void field_type(String cardinality, String expectedOutputPath) throws IOException
    {
        for (String name : commonNameFormats)
        {
            final Property property = Property.createProperty(
                name,
                null, null, emptyList(), false,
                TempNamedType.create(name, cardinality), null, null);

            testTemplateWithNamedElement("field_type", property, expectedOutputPath);
        }
    }
}
