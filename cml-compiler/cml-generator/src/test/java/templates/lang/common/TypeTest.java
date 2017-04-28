package templates.lang.common;

import cml.language.foundation.Type;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TypeTest extends LangTest
{
    private static final Collection<String> primitiveTypeNames = unmodifiableCollection(asList(
        "Boolean", "Integer", "Decimal", "String", "Regex", // main primitive types
        "Byte", "Short", "Long", "Float", "Double", "Char" // remaining primitive types
    ));

    public TypeTest(String targetLanguageExtension)
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
        for (String typeName : primitiveTypeNames)
        {
            final Type type = Type.create(typeName, null);

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

        template.add("named_element", Type.create(name, null));

        final String result = template.render();

        assertThat(result, is(pascalCase(name)));
    }

}
