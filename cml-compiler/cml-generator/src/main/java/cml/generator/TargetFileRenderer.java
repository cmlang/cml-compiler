package cml.generator;

import cml.io.Console;
import cml.io.FileSystem;
import cml.language.features.Target;
import cml.templates.TemplateRenderer;

import java.io.File;
import java.util.Map;
import java.util.Set;

public interface TargetFileRenderer
{
    void renderTargetFiles(Target target, String targetDirPath, String fileType, Map<String, Object> templateArgs);

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
        Target target,
        String targetDirPath,
        String fileType,
        Map<String, Object> templateArgs)
    {
        final Set<TargetFile> targetFiles = targetFileRepository.findTargetFiles(target, fileType, templateArgs);

        targetFiles.forEach(targetFile -> renderTargetFile(targetFile, targetDirPath, templateArgs));
    }

    private void renderTargetFile(
        final TargetFile targetFile,
        final String targetDirPath,
        final Map<String, Object> args)
    {
        console.println("- %s", targetFile.getPath());

        if (targetFile.getTemplateFile().isPresent())
        {
            final String targetLanguageExtension = targetFile.getExtension();
            final String contents = templateRenderer.renderTemplate(
                targetFile.getTemplateFile().get(),
                targetFile.getTemplateName(),
                args,
                targetLanguageExtension);

            final String path = targetDirPath + File.separatorChar + targetFile.getPath();
            fileSystem.createFile(path, contents);
        }
    }
}
