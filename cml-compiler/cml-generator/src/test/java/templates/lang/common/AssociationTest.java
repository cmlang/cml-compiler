package templates.lang.common;

import cml.language.features.Association;
import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;

public class AssociationTest extends LangTest
{
    public AssociationTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "association";
    }

    @Test
    public void test_employment() throws IOException
    {
        final Association employment = createEmployment();

        test_association_class(employment, "employment");
    }

    @SuppressWarnings("ConstantConditions")
    private static Association createEmployment()
    {
        final Concept employee = Concept.create("Employee");
        employee.addMember(Property.create("name", Type.STRING));
        employee.addMember(Property.create("employer", Type.create("Organization")));

        final Concept organization = Concept.create("Organization");
        organization.addMember(Property.create("name", Type.STRING));
        organization.addMember(Property.create("employees", Type.create("Employee", "*")));

        final Association employment = Association.create("Employment");
        employment.addMember(AssociationEnd.create("Employee", "employer"));
        employment.addMember(AssociationEnd.create("Organization", "employees"));

        employment.getAssociationEnds().get(0).setConcept(employee);
        employment.getAssociationEnds().get(0).setProperty(employee.getProperty("employer").get());

        employment.getAssociationEnds().get(1).setConcept(organization);
        employment.getAssociationEnds().get(1).setProperty(organization.getProperty("employees").get());

        return employment;
    }

    private void test_association_class(Association association, String expectedOutputPath) throws IOException
    {
        final String templateName = "association_class";
        final ST template = getTemplate(templateName);
        assertNotNull("Expected template: " + templateName, template);

        template.add("association", association);

        final String result = template.render();
        assertThatOutputMatches(expectedOutputPath + "." + getTargetLanguageExtension(), result);
    }

}
