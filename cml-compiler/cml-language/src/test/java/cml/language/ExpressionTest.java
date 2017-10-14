package cml.language;

import cml.io.Console;
import cml.io.FileSystem;
import cml.io.ModuleManager;
import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.foundation.TempModel;
import cml.language.generated.Expression;
import cml.language.generated.Property;
import cml.language.generated.Type;
import cml.language.generated.UndefinedType;
import cml.language.loader.ModelLoader;
import cml.templates.ModelAdaptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static cml.language.functions.ModelFunctions.moduleOf;
import static cml.language.functions.ModuleFunctions.conceptOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.jooq.lambda.Seq.seq;
import static org.junit.Assert.*;

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

    private final File moduleDir;
    private final FileSystem fileSystem;
    private final ModuleManager moduleManager;
    private final ModelLoader modelLoader;
    private final STGroupFile groupFile;

    public ExpressionTest(@SuppressWarnings("unused") String moduleName, File moduleDir)
    {
        final Console console = Console.createSystemConsole();

        this.moduleDir = moduleDir;
        this.fileSystem = FileSystem.create(console);
        this.moduleManager = ModuleManager.create(console, fileSystem);
        this.modelLoader = ModelLoader.create(console, moduleManager);
        this.groupFile = getOclTemplateGroup();
    }

    @Test
    public void expressionOCL() throws Exception
    {
        final TempConcept concept = loadExpressions();
        final Properties expectedOCL = loadProperties("expected_ocl.properties");

        for (final Property property: seq(concept.getProperties()))
        {
            assertExpectedOCL(expectedOCL, property);
        }
    }

    @Test
    public void expectedType() throws Exception
    {
        final TempConcept concept = loadExpressions();
        final Properties expectedType = loadProperties("expected_type.properties");

        for (final Property property: seq(concept.getProperties()))
        {
            assertExpectedType(expectedType, property);
        }
    }

    private TempConcept loadExpressions()
    {
        final String modulesBaseDir = fileSystem.extractParentPath(moduleDir.getPath());

        moduleManager.clearBaseDirs();
        moduleManager.addBaseDir(modulesBaseDir);

        final TempModel model = TempModel.create();
        modelLoader.loadModel(model, moduleDir.getName());

        final String moduleName = moduleDir.getName();
        final Optional<TempModule> module = moduleOf(model, moduleName);
        assertTrue("Module should be found: " + moduleName, module.isPresent());

        final Optional<TempConcept> concept = conceptOf(module.get(), "Expressions");
        assertTrue("The Expressions concept should be found in module: " + moduleName, concept.isPresent());

        return concept.get();
    }

    private Properties loadProperties(String fileName) throws IOException
    {
        final Properties properties = new Properties();
        final File propertiesFile = new File(moduleDir, fileName);

        try (final FileInputStream fileInputStream = new FileInputStream(propertiesFile))
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

        groupFile.registerModelAdaptor(Object.class, new ModelAdaptor());

        return groupFile;
    }

    private void assertExpectedOCL(Properties expectedOCL, Property property)
    {
        final String expectedOCLExpression = expectedOCL.getProperty(property.getName());

        if (expectedOCLExpression == null)
        {
            assertFalse(
                "Expected non-init property or missing property in expected_ocl.properties file: " + property.getName(),
                property.getValue().isPresent());
        }
        else
        {
            assertTrue("Expected value for property: " + property.getName(), property.getValue().isPresent());

            final ST oclTemplate = groupFile.getInstanceOf("ocl");
            oclTemplate.add("expr", property.getValue().get());

            assertEquals(
                "Property should match OCL: " + property.getName(),
                expectedOCLExpression,
                oclTemplate.render());
        }
    }

    private void assertExpectedType(Properties expectedTypes, Property property)
    {
        final String expectedType = expectedTypes.getProperty(property.getName());

        if (expectedType == null)
        {
            assertFalse(
                "Expected non-init property or missing property in expected_type.properties file: " + property.getName(),
                property.getValue().isPresent());
        }
        else 
        {
            assertTrue("Expected type for property: " + property.getName(), property.getValue().isPresent());

            final Expression value = property.getValue().orElse(null);
            assertNotNull("Should have init for property: " + property.getName(), value);

            final Type type = value.getType();
            assertNotNull("Should have computed type for property: " + property.getName(), type);

            if (type instanceof UndefinedType)
            {
                final UndefinedType undefinedType = (UndefinedType) type;

                fail("Type Error of property '" + property.getName() + "': " + undefinedType.getErrorMessage());
            }

            assertEquals(
                "Property should match expected type: " + property.getName(),
                expectedType,
                type.getDiagnosticId());
        }
    }
}
