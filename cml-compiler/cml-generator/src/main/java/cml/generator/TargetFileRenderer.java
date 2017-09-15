package cml.generator;

import cml.io.Console;
import cml.io.FileSystem;
import cml.language.features.Task;
import cml.language.foundation.NamedElement;
import cml.templates.TemplateRenderer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TargetFileRenderer
{
    void renderTargetFiles(Task task, String targetDirPath, String fileType, NamedElement namedElement);

    static TargetFileRenderer create(
        Console console,
        FileSystem fileSystem,
        TargetFileRepository targetFileRepository,
        TemplateRenderer templateRenderer)
    {
        return new TargetFileRendererImpl(console, fileSystem, targetFileRepository, templateRenderer);
    }
}

class TargetFileRendererImpl implements TargetFileRenderer
{
    private static final String TASK = "task";

    private final Console console;
    private final FileSystem fileSystem;
    private final TargetFileRepository targetFileRepository;
    private final TemplateRenderer templateRenderer;

    TargetFileRendererImpl(
        Console console,
        FileSystem fileSystem,
        TargetFileRepository targetFileRepository,
        TemplateRenderer templateRenderer)
    {
        this.console = console;
        this.fileSystem = fileSystem;
        this.targetFileRepository = targetFileRepository;
        this.templateRenderer = templateRenderer;
    }

    @Override
    public void renderTargetFiles(
        final Task task,
        final String targetDirPath,
        final String modelElementType,
        final NamedElement namedElement)
    {
        final Map<String, Object> templateArgs = new HashMap<>();
        templateArgs.put(TASK, getTargetProperties(task));
        templateArgs.put(modelElementType, namedElement);

        final List<TargetFile> targetFiles = targetFileRepository.findTargetFiles(task, modelElementType, templateArgs);

        if (targetFiles.size() > 0)
        {
            if (!namedElement.getName().equals("model"))
            {
                console.println();
            }

            console.println("%s files:", namedElement.getName());

            targetFiles.forEach(targetFile -> renderTargetFile(targetFile, targetDirPath, templateArgs));
        }
    }

    private void renderTargetFile(
        final TargetFile targetFile,
        final String targetDirPath,
        final Map<String, Object> templateArgs)
    {
        console.println("- %s", targetFile.getPath());

        if (targetFile.getTemplateFile().isPresent())
        {
            final String contents = templateRenderer.renderTemplate(
                targetFile.getTemplateFile().get(),
                targetFile.getTemplateName(),
                templateArgs);

            final String path = targetDirPath + File.separatorChar + targetFile.getPath();
            fileSystem.createFile(path, contents);
        }
        else
        {
            console.println(" (not found template for: %s)", targetFile.getPath());
        }
    }

    private static Map<String, Object> getTargetProperties(final Task task)
    {
        final Map<String, Object> properties = new HashMap<>();

        //noinspection ConstantConditions
        task.getProperties()
            .stream()
            .filter(property -> property.getValue().isPresent())
            .forEach(property -> properties.put(property.getName(), property.getValue().get()));

        return properties;
    }
}
