package cml.language;

import cml.io.Console;
import cml.io.Directory;
import cml.io.FileSystem;
import cml.language.features.Concept;
import cml.language.foundation.Model;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ModelLoaderTest
{
    private FileSystem fileSystem;
    private ModelLoader modelLoader;

    @Before
    public void setUp()
    {
        fileSystem = FileSystem.create();
        modelLoader = ModelLoader.create(Console.create(), fileSystem);
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

    private Concept loadConcept(String sourceFileName)
    {
        return loadModel(sourceFileName).getConcepts().get(0);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Model loadModel(String dirName)
    {
        final String path = "src/test/resources/cml/language/ModelLoader/" + dirName;
        final Directory sourceDir = fileSystem.findDirectory(path).get();
        final Model model = Model.create();

        modelLoader.loadModel(model, sourceDir);

        return model;
    }
}