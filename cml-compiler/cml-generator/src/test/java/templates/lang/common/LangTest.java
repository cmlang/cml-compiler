package templates.lang.common;

import cml.language.features.TempConcept;
import cml.language.generated.Association;
import cml.language.generated.NamedElement;
import cml.language.generated.Property;
import cml.language.generated.Type;
import com.google.common.io.Resources;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(Parameterized.class)
public abstract class LangTest extends TemplateTest
{
    private static final Charset OUTPUT_FILE_ENCODING = Charset.forName("UTF-8");
    private static final String EXPECTED_OUTPUT_PATH = "/%s/%s/%s/%s";
    private static final String LANG_GROUP_PATH = "lang/%s";
    private static final String TEMPLATES_LANG = "templates/lang";

    @Parameters
    public static Collection<String> targetLanguageExtension()
    {
        return asList("java", "py");
    }

    private final String targetLanguageExtension;

    LangTest(String targetLanguageExtension)
    {
        this.targetLanguageExtension = targetLanguageExtension;
    }

    protected abstract String getExpectedOutputPath();

    private String getTargetLanguageExtension()
    {
        return targetLanguageExtension;
    }

    @Override
    protected String getTemplatePath()
    {
        return format(LANG_GROUP_PATH, targetLanguageExtension);
    }

    void testAssociationClass(Association association, String expectedOutputPath) throws IOException
    {
        final String templateName = "association_class";
        final ST template = getTemplate(templateName);
        assertNotNull("Expected template: " + templateName, template);

        template.add("association", association);

        final String result = template.render();
        assertThatOutputMatches(expectedOutputPath + "." + getTargetLanguageExtension(), result);
    }

    void testConceptClass(TempConcept concept, String expectedOutputPath) throws IOException
    {
        final String templateName = "class";
        final ST template = getTemplate(templateName);
        assertNotNull("Expected template: " + templateName, template);

        template.add("concept", concept);

        final String result = template.render();
        assertThatOutputMatches(expectedOutputPath + "." + getTargetLanguageExtension(), result);
    }

    void testTemplateWithConcept(String templateName, TempConcept concept, String expectedOutputPath)
        throws IOException
    {
        testTemplate(templateName, "concept", concept, expectedOutputPath);
    }

    void testTemplateWithProperty(String templateName, Property property, String expectedOutputPath)
        throws IOException
    {
        testTemplate(templateName, "property", property, expectedOutputPath);
    }

    void testTemplateWithType(String templateName, Type type, String expectedOutputPath) throws IOException
    {
        testTemplate(templateName, "type", type, expectedOutputPath);
    }

    void testTemplateWithNamedElement(String templateName, NamedElement namedElement, String expectedOutputPath)
        throws IOException
    {
        testTemplate(templateName, "named_element", namedElement, expectedOutputPath);
    }

    void testTemplateWithNamedElement(String templateName, Type type, String expectedOutputPath)
        throws IOException
    {
        testTemplate(templateName, "named_element", type, expectedOutputPath);
    }

    void assertThatOutputMatches(String expectedOutputPath, String actualOutput) throws IOException
    {
        expectedOutputPath = format(
            EXPECTED_OUTPUT_PATH,
            TEMPLATES_LANG,
            targetLanguageExtension,
            getExpectedOutputPath(),
            expectedOutputPath);

        final URL expectedOutputResource = getClass().getResource(expectedOutputPath);
        assertNotNull("Expected output resource must exist: " + expectedOutputPath, expectedOutputResource);

        final String expectedOutput = Resources.toString(expectedOutputResource, OUTPUT_FILE_ENCODING);
        assertEquals(expectedOutputPath, expectedOutput, actualOutput);
    }

    private void testTemplate(String templateName, String paramName, Object paramValue, String expectedOutputPath)
        throws IOException
    {
        if (!getExpectedOutputPath().endsWith(templateName))
        {
            expectedOutputPath = templateName + File.separator + expectedOutputPath;
        }

        final ST template = getTemplate(templateName);
        assertNotNull("Expected template: " + templateName, template);

        template.add(paramName, paramValue);

        final String result = template.render();

        assertThatOutputMatches(expectedOutputPath, result);
    }
}
