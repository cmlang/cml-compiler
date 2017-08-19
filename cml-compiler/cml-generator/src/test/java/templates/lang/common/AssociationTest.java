package templates.lang.common;

import cml.language.Model;
import cml.language.features.Association;
import cml.language.features.AssociationEnd;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.Property;
import cml.language.types.NamedType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

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
        final Concept employee = createEmployee();
        final Concept organization = createOrganization();
        final Association employment = createEmployment(employee, organization);

        testAssociationClass(employment, "employment");
        testConceptClass(employee, "employee");
        testConceptClass(organization, "organization");
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
        organization.addMember(Property.create("name", NamedType.STRING));
        organization.addMember(Property.create("employees", NamedType.create("Employee", "*")));
        return organization;
    }

    @NotNull
    private static Concept createEmployee()
    {
        final Concept employee = Concept.create("Employee");
        employee.addMember(Property.create("name", NamedType.STRING));
        employee.addMember(Property.create("employer", NamedType.create("Organization")));
        return employee;
    }
}
