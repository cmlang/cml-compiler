package cml.language;

import cml.io.Console;
import cml.io.FileSystem;
import cml.language.expressions.Literal;
import cml.language.features.*;
import cml.language.foundation.Property;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ModelLoaderTest
{
    private static final String BASE_PATH = "src/test/resources/cml/language/ModelLoader/";

    private FileSystem fileSystem;
    private ModelLoader modelLoader;

    @Before
    public void setUp()
    {
        fileSystem = FileSystem.create(Console.create());
        modelLoader = ModelLoader.create(Console.create(), fileSystem);
    }

    @Test
    public void module()
    {
        final String moduleName = "module_name";
        final Module module = loadModule(moduleName);
        final Concept concept = module.getConcepts().get(0);

        assertThat(module.getName(), is(moduleName));
        assertThat(concept.getName(), is("SomeConcept"));

        final String anotherModuleName = "another_module";
        final Import _import = module.getImports().get(0);
        final Module anotherModule = _import.getModule().get();
        final Concept anotherConcept = anotherModule.getConcepts().get(0);

        assertThat(_import.getName(), is(anotherModuleName));
        assertThat(anotherModule.getName(), is(anotherModuleName));
        assertThat(anotherConcept.getName(), is("AnotherConcept"));
        assertThat(concept.getDirectAncestors().get(0), is(sameInstance(anotherConcept)));
    }

    @Test
    public void invalid_module_name()
    {
        final Model model = Model.create();
        final String modulePath = BASE_PATH + "invalid_module_name";

        final int result = modelLoader.loadModel(model, modulePath);

        assertThat(result, is(3));
    }

    @Test
    public void concrete_concept()
    {
        final Concept concept = loadConcept("concrete_concept");

        assertThat(concept.getName(), is("ModelElement"));
        assertFalse("Concept should be concrete.", concept.isAbstract());
    }

    @Test
    public void derived_property()
    {
        final Concept concept = loadConcept("derived_property");

        assertThat(concept.getName(), is("SomeConcept"));
        assertTrue("derivedProperty should be derived.", concept.getProperty("derivedProperty").get().isDerived());
        assertFalse("nonDerivedProperty should not be derived.", concept.getProperty("nonDerivedProperty").get().isDerived());
    }

    @Test
    public void abstract_concept()
    {
        final Concept concept = loadConcept("abstract_concept");

        assertThat(concept.getName(), is("ModelElement"));
        assertTrue("Concept should be abstract.", concept.isAbstract());
    }

    @Test
    public void expressions()
    {
        final Concept concept = loadConcept("expressions");

        assertThat(concept.getName(), is("Expressions"));

        assertPropertyFound(concept, "str", "SomeString");
        assertPropertyFound(concept, "int", "123");
        assertPropertyFound(concept, "dec", "123.456");
    }

    @Test
    public void associations()
    {
        final Association employment = loadAssociation("Employment");
        assertAssociationEndFound(employment, "Employee", "employer");
        assertAssociationEndFound(employment, "Organization", "employees");

        final Association vehicleOwnership = loadAssociation("VehicleOwnership");
        assertAssociationEndFound(vehicleOwnership, "Vehicle", "owner", Type.create("Organization"));
        assertAssociationEndFound(vehicleOwnership, "Organization", "fleet", Type.create("Vehicle", "*"));
    }

    private Module loadModule(String sourceFileName)
    {
        return loadModel(sourceFileName).getModules().get(0);
    }

    private Concept loadConcept(String sourceFileName)
    {
        return loadModel(sourceFileName).getConcepts().get(0);
    }

    private Association loadAssociation(String name)
    {
        final Optional<Association> association = loadModel("associations").getAssociation(name);

        assert association.isPresent();
        
        return association.get();
    }

    private Model loadModel(String moduleName)
    {
        final Model model = Model.create();
        final String modulePath = BASE_PATH + moduleName;

        modelLoader.loadModel(model, modulePath);

        return model;
    }

    private void assertPropertyFound(Concept concept, String propertyName, String propertyValue)
    {
        final Property str = concept.getProperty(propertyName).orElse(null);
        assertNotNull(propertyName, str);

        final Literal literal = (Literal)str.getValue().orElse(null);
        assertNotNull(propertyName, literal);

        assertThat(propertyName, literal.getText(), is(propertyValue));
    }

    private void assertAssociationEndFound(Association association, String conceptName, String propertyName)
    {
        assertAssociationEndFound(association, conceptName, propertyName, null);
    }

    private void assertAssociationEndFound(
        Association association,
        String conceptName, String propertyName,
        @Nullable Type expectedType)
    {
        final AssociationEnd associationEnd = association.getAssociationEnd(conceptName, propertyName).orElse(null);
        assertNotNull(conceptName + "." + propertyName, associationEnd);
        assertEquals(conceptName + "." + propertyName, expectedType, associationEnd.getPropertyType().orElse(null));
        assertTrue(conceptName, associationEnd.getConcept().isPresent());
        assertTrue(conceptName + "." + propertyName, associationEnd.getProperty().isPresent());

        final Concept concept = associationEnd.getConcept().get();
        assertEquals(conceptName, concept.getName(), conceptName);

        final Property property = associationEnd.getProperty().get();
        assertEquals(conceptName + "." + propertyName, property.getName(), propertyName);

        assertTrue(conceptName + "." + propertyName, concept.getMembers().contains(property));
    }
}