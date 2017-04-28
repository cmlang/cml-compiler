package cml.generator;

import cml.io.Console;
import cml.io.Directory;
import cml.io.FileSystem;
import cml.io.ModuleManager;
import cml.language.Model;
import cml.language.ModelVisitor;
import cml.language.features.Task;
import cml.templates.TemplateGroupFile;
import cml.templates.TemplateRenderer;
import cml.templates.TemplateRepository;

import java.util.Optional;

public interface Generator
{
    int generate(Model model, final String targetName, final String targetDirPath);

    static Generator create(Console console, FileSystem fileSystem, ModuleManager moduleManager)
    {
        final TemplateRepository templateRepository = TemplateRepository.create(moduleManager);
        final TemplateRenderer templateRenderer = TemplateRenderer.create(console);
        final TargetFileRepository targetFileRepository = TargetFileRepository.create(templateRepository, templateRenderer);
        final TargetFileRenderer targetFileRenderer = TargetFileRenderer.create(console, fileSystem, targetFileRepository, templateRenderer);
        return new GeneratorImpl(console, fileSystem, moduleManager, targetFileRepository, targetFileRenderer);
    }
}

class GeneratorImpl implements Generator
{
    private static final int SUCCESS = 0;
    private static final int FAILURE__CONSTRUCTOR_UNKNOWN = 101;
    private static final int FAILURE__CONSTRUCTOR_UNDEFINED = 102;
    private static final int FAILURE__TASK_UNDECLARED = 103;

    private static final String NO_SOURCE_FILE_HAS_DECLARED_TASK = "No source file has declared task named: %s";
    private static final String NO_CONSTRUCTOR_DEFINED_FOR_TASK = "No constructor defined for task: %s";
    private static final String NO_TEMPLATES_FOUND_FOR_CONSTRUCTOR = "No templates found for constructor: %s";

    private final Console console;
    private final FileSystem fileSystem;
    private final ModuleManager moduleManager;
    private final TargetFileRepository targetFileRepository;
    private final TargetFileRenderer targetFileRenderer;

    GeneratorImpl(
        Console console,
        FileSystem fileSystem,
        ModuleManager moduleManager,
        TargetFileRepository targetFileRepository,
        TargetFileRenderer targetFileRenderer)
    {
        this.console = console;
        this.fileSystem = fileSystem;
        this.moduleManager = moduleManager;
        this.targetFileRepository = targetFileRepository;
        this.targetFileRenderer = targetFileRenderer;
    }

    @Override
    public int generate(Model model, final String targetName, final String targetDirPath)
    {
        TemplateGroupFile.setModuleManager(moduleManager);

        final Optional<Task> target = model.getTarget(targetName);
        if (!target.isPresent())
        {
            console.println(NO_SOURCE_FILE_HAS_DECLARED_TASK, targetName);
            return FAILURE__TASK_UNDECLARED;
        }

        final Optional<String> targetType = target.get().getType();
        if (!targetType.isPresent())
        {
            console.println(NO_CONSTRUCTOR_DEFINED_FOR_TASK, target.get().getName());
            return FAILURE__CONSTRUCTOR_UNDEFINED;
        }

        if (!targetFileRepository.templatesFoundFor(target.get()))
        {
            console.println(NO_TEMPLATES_FOUND_FOR_CONSTRUCTOR, targetType.get());
            return FAILURE__CONSTRUCTOR_UNKNOWN;
        }

        final Optional<Directory> targetDir = fileSystem.findDirectory(targetDirPath);
        targetDir.ifPresent(fileSystem::cleanDirectory);

        final TargetGenerator targetGenerator = new TargetGenerator(
            console, targetFileRenderer,
            target.get(), targetDirPath
        );
        final ModelVisitor modelVisitor = new ModelVisitor(targetGenerator);

        modelVisitor.visit(model);

        return SUCCESS;
    }

}

