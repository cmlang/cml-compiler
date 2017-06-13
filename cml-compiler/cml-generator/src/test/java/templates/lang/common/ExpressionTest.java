package templates.lang.common;

import cml.io.Console;
import cml.io.FileSystem;
import cml.language.Model;
import cml.language.ModelLoader;
import cml.language.features.Association;
import cml.language.features.Concept;
import org.junit.Test;

import java.io.IOException;

public class ExpressionTest extends LangTest
{
    private static final String MODULE_PATH = "./src/test/resources/modules/expressions";

    public ExpressionTest(String targetLanguage)
    {
        super(targetLanguage);
    }

    @Override
    protected String getExpectedOutputPath()
    {
        return "expressions";
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void test_expressions() throws IOException
    {
        final Model model = loadModel();
        final Concept employee = model.getConcept("Employee").get();
        final Concept organization = model.getConcept("Organization").get();
        final Association employment = model.getAssociation("Employment").get();

        testAssociationClass(employment, "employment");
        testConceptClass(employee, "employee");
        testConceptClass(organization, "organization");
    }

    private Model loadModel()
    {
        final FileSystem fileSystem = FileSystem.create(Console.create());
        final ModelLoader modelLoader = ModelLoader.create(Console.create(), fileSystem);

        final Model model = Model.create();
        modelLoader.loadModel(model, MODULE_PATH);

        return model;
    }

}
