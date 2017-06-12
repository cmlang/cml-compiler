package templates.lang.common;

import cml.language.Model;
import cml.language.features.Association;
import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;

public class ExpressionTest extends LangTest
{
    public ExpressionTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "expressions";
    }

    @Test
    public void test_expressions() throws IOException
    {
        final Concept employee = createEmployee();
        final Concept organization = createOrganization();
        final Association employment = createEmployment(employee, organization);

        test_association_class(employment, "employment");
        test_concept_class(employee, "employee");
        test_concept_class(organization, "organization");
    }

    @SuppressWarnings("ConstantConditions")
    private static Association createEmployment(Concept employee, Concept organization)
    {
        final Association employment = Association.create("Employment");
        employment.addMember(AssociationEnd.create("Employee", "employer"));
        employment.addMember(AssociationEnd.create("Organization", "employees"));

        employment.getAssociationEnds().get(0).setConcept(employee);
        employment.getAssociationEnds().get(0).setProperty(employee.getProperty("employer").get());

        employment.getAssociationEnds().get(1).setConcept(organization);
        employment.getAssociationEnds().get(1).setProperty(organization.getProperty("employees").get());

        final Model model = Model.create();
        final Module module = Module.create("some_module");

        model.addMember(module);
        module.addMember(employee);
        module.addMember(organization);
        module.addMember(employment);

        return employment;
    }

    @NotNull
    private static Concept createOrganization()
    {
        final Concept organization = Concept.create("Organization");
        organization.addMember(Property.create("name", Type.STRING));
        organization.addMember(Property.create("employees", Type.create("Employee", "*")));
        return organization;
    }

    @NotNull
    private static Concept createEmployee()
    {
        final Concept employee = Concept.create("Employee");
        employee.addMember(Property.create("name", Type.STRING));
        employee.addMember(Property.create("employer", Type.create("Organization")));
        return employee;
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

    private void test_concept_class(Concept concept, String expectedOutputPath) throws IOException
    {
        final String templateName = "class";
        final ST template = getTemplate(templateName);
        assertNotNull("Expected template: " + templateName, template);

        template.add("concept", concept);

        final String result = template.render();
        assertThatOutputMatches(expectedOutputPath + "." + getTargetLanguageExtension(), result);
    }
}
