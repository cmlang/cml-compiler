package cml.generator;

import cml.io.Console;
import cml.io.Directory;
import cml.io.FileSystem;
import cml.language.ModelVisitor;
import cml.language.features.Target;
import cml.language.Model;
import cml.templates.TemplateRenderer;
import cml.templates.TemplateRepository;

import java.util.Optional;

public interface Generator
{
    int generate(Model model, final String targetName, final String targetDirPath);

    static Generator create(Console console, FileSystem fileSystem)
    {
        final TemplateRepository templateRepository = TemplateRepository.create();
        final TemplateRenderer templateRenderer = TemplateRenderer.create(console);
        final TargetFileRepository targetFileRepository = TargetFileRepository.create(templateRepository, templateRenderer);
        final TargetFileRenderer targetFileRenderer = TargetFileRenderer.create(console, fileSystem, targetFileRepository, templateRenderer);
        return new GeneratorImpl(console, fileSystem, targetFileRepository, targetFileRenderer);
    }
}

class GeneratorImpl implements Generator
{
    private static final int SUCCESS = 0;
    private static final int FAILURE__TARGET_TYPE_UNKNOWN = 101;
    private static final int FAILURE__TARGET_TYPE_UNDEFINED = 102;
    private static final int FAILURE__TARGET_NAME_UNDECLARED = 103;

    private final Console console;
    private final FileSystem fileSystem;
    private final TargetFileRepository targetFileRepository;
    private final TargetFileRenderer targetFileRenderer;

    GeneratorImpl(
        Console console,
        FileSystem fileSystem,
        TargetFileRepository targetFileRepository,
        TargetFileRenderer targetFileRenderer)
    {
        this.console = console;
        this.fileSystem = fileSystem;
        this.targetFileRepository = targetFileRepository;
        this.targetFileRenderer = targetFileRenderer;
    }

    @Override
    public int generate(Model model, final String targetName, final String targetDirPath)
    {
        final Optional<Target> target = model.getTarget(targetName);
        if (!target.isPresent())
        {
            console.println("Source files have not declared target name: %s", targetName);
            return FAILURE__TARGET_NAME_UNDECLARED;
        }

        final Optional<String> targetType = target.get().getType();
        if (!targetType.isPresent())
        {
            console.println("Target type not defined for target: %s", target.get().getName());
            return FAILURE__TARGET_TYPE_UNDEFINED;
        }

        if (!targetFileRepository.templatesFoundFor(target.get()))
        {
            console.println("No templates found for target type: %s", targetType.get());
            return FAILURE__TARGET_TYPE_UNKNOWN;
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

