package cml.generator;

import cml.language.features.Task;
import cml.templates.TemplateFile;
import cml.templates.TemplateRenderer;
import cml.templates.TemplateRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

interface TargetFileRepository
{
    boolean templatesFoundFor(Task task);
    List<TargetFile> findTargetFiles(Task task, String fileType, Map<String, Object> args);

    static TargetFileRepository create(TemplateRepository templateRepository, TemplateRenderer templateRenderer)
    {
        return new TargetFileRepositoryImpl(templateRepository, templateRenderer);
    }
}

class TargetFileRepositoryImpl implements TargetFileRepository
{
    private static final String STG_EXT = ".stg";
    private static final String GROUP_FILES = "files" + STG_EXT;
    private static final String FILES_SUFFIX = "_files";
    private static final String FILE_LINE_SEPARATOR = "\\|";
    private static final String TEMPLATE_NAME_SEPARATOR = ":";

    // Collaborators:
    private final TemplateRepository templateRepository;
    private final TemplateRenderer templateRenderer;

    TargetFileRepositoryImpl(TemplateRepository templateRepository, TemplateRenderer templateRenderer)
    {
        this.templateRepository = templateRepository;
        this.templateRenderer = templateRenderer;
    }

    @Override
    public boolean templatesFoundFor(Task task)
    {
        return findFilesTemplateForTask(task).isPresent();
    }

    @Override
    public List<TargetFile> findTargetFiles(
        final Task task,
        final String fileType,
        final Map<String, Object> args)
    {
        final Optional<TemplateFile> fileTemplates = findFilesTemplateForTask(task);

        if (fileTemplates.isPresent())
        {
            final String templateName = fileType + FILES_SUFFIX;
            final String files = templateRenderer.renderTemplate(fileTemplates.get(), templateName, args);
            return stream(files.split("\n"))
                .map(line -> line.split(FILE_LINE_SEPARATOR))
                .map(pair -> createTargetFile(pair[1], task.getConstructor().get(), pair[0]))
                .collect(toList());
        }
        else
        {
            return emptyList();
        }
    }

    private Optional<TemplateFile> findFilesTemplateForTask(Task task)
    {
        return templateRepository.findTemplate(task.getConstructor().get(), GROUP_FILES);
    }

    private TargetFile createTargetFile(String path, String targetType, String templateName)
    {
        final String[] pair = templateName.split(TEMPLATE_NAME_SEPARATOR);

        if (pair.length == 2)
        {
            targetType = pair[0];
            templateName = pair[1];
        }

        final TargetFile targetFile = new TargetFile(path, templateName);
        final Optional<TemplateFile> templateFile = templateRepository.findTemplate(targetType, templateName + STG_EXT);

        templateFile.ifPresent(targetFile::setTemplateFile);

        return targetFile;
    }

}

