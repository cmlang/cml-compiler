package patterns;

import cml.language.foundation.Type;
import generic.TemplateTest;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TypeTest extends TemplateTest
{
    @Override
    protected String getTemplatePath()
    {
        return "patterns/type";
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
