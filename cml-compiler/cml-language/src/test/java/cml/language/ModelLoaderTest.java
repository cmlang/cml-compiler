package cml.language;

import cml.io.Console;
import cml.io.FileSystem;
import cml.io.ModuleManager;
import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.foundation.TempModel;
import cml.language.generated.*;
import cml.language.loader.ModelLoader;
import cml.language.types.TempNamedType;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static cml.language.functions.ConceptFunctions.propertyOf;
import static cml.language.functions.ModelFunctions.associationOf;
import static cml.language.functions.TypeFunctions.isEqualTo;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class ModelLoaderTest
{
    private static final String BASE_PATH = "src/test/resources/cml/language/ModelLoader/";

    private FileSystem fileSystem;
    private ModuleManager moduleManager;
    private ModelLoader modelLoader;

    @Before
    public void setUp()
    {
        final Console systemConsole = Console.createSystemConsole();

        fileSystem = FileSystem.create(systemConsole);
        moduleManager = ModuleManager.create(systemConsole, fileSystem);
        modelLoader = ModelLoader.create(systemConsole, moduleManager);
    }

    @Test
    public void module()
    {
        final String moduleName = "module_name";
        final TempModule module = loadModule(moduleName);
        final Concept concept = module.getConcepts().get(0);

        assertThat(module.getName(), is(moduleName));
        assertThat(concept.getName(), is("SomeConcept"));

        final String anotherModuleName = "another_module";
        final Import _import = module.getImports().get(0);
        final TempModule anotherModule = (TempModule) _import.getImportedModule();
        final Concept anotherConcept = anotherModule.getConcepts().get(0);

        assertThat(_import.getName(), is(anotherModuleName));
        assertThat(anotherModule.getName(), is(anotherModuleName));
        assertThat(anotherConcept.getName(), is("AnotherConcept"));
        assertThat(concept.getAncestors().get(0), is(sameInstance(anotherConcept)));
    }

    @Test
    public void invalid_module_name()
    {
        final String moduleName = "invalid_module_name";
        final String modulePath = BASE_PATH + moduleName;
        final String modulesBaseDir = fileSystem.extractParentPath(modulePath);

        moduleManager.clearBaseDirs();
        moduleManager.addBaseDir(modulesBaseDir);

        final TempModel model = TempModel.create();
        final int result = modelLoader.loadModel(model, moduleName);

        assertThat(result, is(3));
    }

    @Test
    public void concrete_concept()
    {
        final TempConcept concept = loadConcept("concrete_concept");

        assertThat(concept.getName(), is("ModelElement"));
        assertFalse("Concept should be concrete.", concept.isAbstraction());
    }

    @Test
    public void derived_property()
    {
        final TempConcept concept = loadConcept("derived_property");

        assertThat(concept.getName(), is("SomeConcept"));
        assertTrue("derivedProperty should be derived.", propertyOf(concept, "derivedProperty").get().isDerived());
        assertFalse("nonDerivedProperty should not be derived.", propertyOf(concept,"nonDerivedProperty").get().isDerived());
    }

    @Test
    public void abstract_concept()
    {
        final TempConcept concept = loadConcept("abstract_concept");

        assertThat(concept.getName(), is("ModelElement"));
        assertTrue("Concept should be abstract.", concept.isAbstraction());
    }

    @Test
    public void expressions()
    {
        final TempConcept concept = loadConcept("expressions");

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
        assertAssociationEndFound(vehicleOwnership, "Vehicle", "owner", TempNamedType.create("Organization"));
        assertAssociationEndFound(vehicleOwnership, "Organization", "fleet", TempNamedType.create("Vehicle", "*"));
    }

    private TempModule loadModule(String sourceFileName)
    {
        return (TempModule) loadModel(sourceFileName).getModules().get(0);
    }

    private TempConcept loadConcept(String sourceFileName)
    {
        return (TempConcept) loadModel(sourceFileName).getConcepts().get(0);
    }

    private Association loadAssociation(String name)
    {
        final Optional<Association> association = associationOf(loadModel("associations"), name);

        assert association.isPresent();
        
        return association.get();
    }

    private TempModel loadModel(String moduleName)
    {
        final String modulePath = BASE_PATH + moduleName;
        final String modulesBaseDir = fileSystem.extractParentPath(modulePath);

        moduleManager.clearBaseDirs();
        moduleManager.addBaseDir(modulesBaseDir);

        final TempModel model = TempModel.create();

        modelLoader.loadModel(model, moduleName);

        return model;
    }

    private void assertPropertyFound(TempConcept concept, String propertyName, String propertyValue)
    {
        final Property str = propertyOf(concept, propertyName).orElse(null);
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
        @Nullable TempNamedType expectedType)
    {
        final AssociationEnd associationEnd = association.getAssociationEnds().stream()
                                                         .filter(associationEnd1 -> associationEnd1.getConceptName().equals(conceptName))
                                                         .filter(associationEnd2 -> associationEnd2.getPropertyName().equals(propertyName))
                                                         .findFirst().orElse(null);

        assertNotNull(conceptName + "." + propertyName, associationEnd);
        assertTrue(conceptName, associationEnd.getAssociatedConcept().isPresent());
        assertTrue(conceptName + "." + propertyName, associationEnd.getAssociatedProperty().isPresent());

        if (expectedType == null)
        {
            assertFalse("Did not expect type for: " + conceptName + "." + propertyName, associationEnd.getPropertyType().isPresent());
        }
        else
        {
            assertTrue("Did expect type for: " + conceptName + "." + propertyName, associationEnd.getPropertyType().isPresent());
            assertTrue("Expected matching type for: " + conceptName + "." + propertyName, isEqualTo(expectedType, associationEnd.getPropertyType().get()));
        }

        final TempConcept concept = associationEnd.getAssociatedConcept().map(c -> (TempConcept) c).get();
        assertEquals(conceptName, concept.getName(), conceptName);

        final Property property = associationEnd.getAssociatedProperty().get();
        assertEquals(conceptName + "." + propertyName, property.getName(), propertyName);

        assertTrue(conceptName + "." + propertyName, concept.getMembers().contains(property));
    }
}