package templates.lang.common;


import cml.io.Console;
import cml.io.FileSystem;
import cml.io.ModuleManager;
import cml.templates.NameRenderer;
import cml.templates.TemplateGroupFile;
import org.junit.Before;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.Collection;
import java.util.Locale;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

@RunWith(Theories.class)
public abstract class TemplateTest
{
    static final Collection<String> commonNameFormats = unmodifiableCollection(asList(
        "SomeName",
        "someName",
        "some_name",
        "some-name"
    ));

    private static final String TEMPLATE_GROUP_PATH = "%s:/%s.stg";
    private static final String MODULE_NAME = "cml_base";
    private static final String CML_MODULES_BASE_DIR = "../../cml-modules";

    private STGroupFile groupFile;

    @Before
    public void setUp()
    {
        groupFile = createTemplateGroupFile(getTemplatePath());

        groupFile.registerRenderer(String.class, new NameRenderer());
    }

    ST getTemplate(String templateName)
    {
        return groupFile.getInstanceOf(templateName);
    }

    /**
     * The path to the template file without the file extension.
     * <p>
     * Used to load the template file and to load the expected output files.
     **/
    protected abstract String getTemplatePath();

    static String pascalCase(String name)
    {
        return NameRenderer.pascalCase(Locale.getDefault(), name);
    }

    static String camelCase(String name)
    {
        return NameRenderer.camelCase(Locale.getDefault(), name);
    }

    static String underscoreCase(String name)
    {
        return NameRenderer.underscoreCase(Locale.getDefault(), name);
    }

    static TemplateGroupFile createTemplateGroupFile(String templatePath)
    {
        final Console console = Console.createSystemConsole();
        final FileSystem fileSystem = FileSystem.create(console);
        final ModuleManager moduleManager = ModuleManager.create(console, fileSystem);

        moduleManager.addBaseDir(CML_MODULES_BASE_DIR);

        TemplateGroupFile.setModuleManager(moduleManager);

        return new TemplateGroupFile(format(TEMPLATE_GROUP_PATH, MODULE_NAME, templatePath));
    }
}
