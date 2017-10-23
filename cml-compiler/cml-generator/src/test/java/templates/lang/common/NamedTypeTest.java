package templates.lang.common;

import cml.language.generated.ValueType;
import cml.language.types.TempNamedType;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.IOException;

import static cml.primitives.Types.PRIMITIVE_TYPE_NAMES;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NamedTypeTest extends LangTest
{
    public NamedTypeTest(String targetLanguageExtension)
    {
        super(targetLanguageExtension);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "type";
    }

    @Test
    public void primitive_types() throws IOException
    {
        for (String typeName : PRIMITIVE_TYPE_NAMES)
        {
            final ValueType type = ValueType.createValueType("", typeName);

            testTemplateWithNamedElement("type_name", type, typeName + ".txt");
        }
    }

    @Test
    public void type_name()
    {
        for (String name : commonNameFormats)
        {
            type_name(name);
        }
    }

    private void type_name(String name)
    {
        final ST template = getTemplate("type_name");

        template.add("named_element", TempNamedType.create(name, null));

        final String result = template.render();

        assertThat(result, is(pascalCase(name)));
    }

}
