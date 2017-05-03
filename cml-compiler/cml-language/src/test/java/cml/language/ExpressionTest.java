package cml.language;

import cml.io.Console;
import cml.io.FileSystem;
import cml.language.features.Concept;
import cml.language.features.Module;
import cml.language.foundation.Property;
import cml.templates.OptionalValueAdaptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ExpressionTest
{
    private static final String BASE_PATH = "./src/test/resources/cml/language/ExpressionTest";
    private static final String ENCODING = "UTF-8";
    private static final char START_CHAR = '<';
    private static final char STOP_CHAR = '>';

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> modulePaths()
    {
        final File file = new File(BASE_PATH);
        final File[] files = file.listFiles(File::isDirectory);
        
        return stream(files == null ? new File[0] : files)
                .map(f -> new Object[] { f.getName(), f })
                .collect(toList());
    }

    private final File modulePath;
    private final FileSystem fileSystem;
    private final ModelLoader modelLoader;
    private final STGroupFile groupFile;

    public ExpressionTest(@SuppressWarnings("unused") String moduleName, File modulePath)
    {
        this.modulePath = modulePath;
        this.fileSystem = FileSystem.create(Console.create());
        this.modelLoader = ModelLoader.create(Console.create(), fileSystem);
        this.groupFile = getOclTemplateGroup();
    }

    @Test
    public void shouldMatchOCL() throws Exception
    {
        final Concept concept = loadExpressions();
        final Properties expectedOCL = loadExpectedOCL();

        for (final Property property: concept.getProperties())
        {
            final String expectedOCLExpression = expectedOCL.getProperty(property.getName());

            assertTrue("Expected value for property: " + property.getName(), property.getValue().isPresent());

            final ST oclTemplate = groupFile.getInstanceOf("ocl");
            oclTemplate.add("expr", property.getValue().get());

            assertEquals(
                "Property should match OCL: " + property.getName(),
                expectedOCLExpression,
                oclTemplate.render());
        }
    }

    private Concept loadExpressions()
    {
        final Model model = Model.create();
        modelLoader.loadModel(model, modulePath.getPath());

        final String moduleName = modulePath.getName();
        final Optional<Module> module = model.getModule(moduleName);
        assertTrue("Module should be found: " + moduleName, module.isPresent());

        final Optional<Concept> concept = module.get().getConcept("Expressions");
        assertTrue("The Expressions concept should be found in module: " + moduleName, concept.isPresent());

        return concept.get();
    }

    private Properties loadExpectedOCL() throws IOException
    {
        final Properties properties = new Properties();
        final File expectedOCLFile = new File(modulePath, "expected_ocl.properties");
        
        try (final FileInputStream fileInputStream = new FileInputStream(expectedOCLFile))
        {
            properties.load(fileInputStream);
        }

        return properties;
    }

    private static STGroupFile getOclTemplateGroup()
    {
        final STGroupFile groupFile = new STGroupFile(
            BASE_PATH + File.separator + "ocl.stg",
            ENCODING, START_CHAR, STOP_CHAR);

        groupFile.registerModelAdaptor(Object.class, new OptionalValueAdaptor());

        return groupFile;
    }
}
