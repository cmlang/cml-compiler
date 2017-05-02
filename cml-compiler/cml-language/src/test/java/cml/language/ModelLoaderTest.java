package cml.language;

import cml.io.Console;
import cml.io.FileSystem;
import cml.language.expressions.Literal;
import cml.language.features.Concept;
import cml.language.features.Import;
import cml.language.features.Module;
import cml.language.foundation.Property;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
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

    private Module loadModule(String sourceFileName)
    {
        return loadModel(sourceFileName).getModules().get(0);
    }

    private Concept loadConcept(String sourceFileName)
    {
        return loadModel(sourceFileName).getConcepts().get(0);
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
}