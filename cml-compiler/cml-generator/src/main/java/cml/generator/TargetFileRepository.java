package cml.generator;

import cml.language.features.Module;
import cml.language.features.Task;
import cml.templates.TemplateFile;
import cml.templates.TemplateRenderer;
import cml.templates.TemplateRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cml.language.functions.ModelElementFunctions.moduleOf;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

interface TargetFileRepository
{
    boolean templatesFoundFor(Task task);
    List<TargetFile> findTargetFiles(Task task, String fileType, Map<String, Object> templateArgs);

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
        final String modelElementType,
        final Map<String, Object> templateArgs)
    {
        final Optional<TemplateFile> fileTemplates = findFilesTemplateForTask(task);

        if (fileTemplates.isPresent() && moduleOf(task).isPresent())
        {
            final String moduleName = fileTemplates.get().getModuleName();
            final Optional<Module> module = moduleOf(task).get().getSelfOrImportedModule(moduleName);

            if (module.isPresent() && task.getConstructor().isPresent())
            {
                final String templateName = modelElementType + FILES_SUFFIX;
                final String files = templateRenderer.renderTemplate(fileTemplates.get(), templateName, templateArgs);

                if (files.trim().length() > 0)
                {
                    final String constructorName = task.getConstructor().get();

                    return stream(files.split("\n"))
                        .map(line -> line.split(FILE_LINE_SEPARATOR))
                        .map(pair -> createTargetFile(module.get(), constructorName, pair[1], pair[0]))
                        .collect(toList());
                }
            }
        }

        return emptyList();
    }

    private Optional<TemplateFile> findFilesTemplateForTask(Task task)
    {
        if (moduleOf(task).isPresent() & task.getConstructor().isPresent())
        {
            final Module module = moduleOf(task).get();
            final String constructorName = task.getConstructor().get();

            return findTemplateFile(module, constructorName, GROUP_FILES);
        }

        return Optional.empty();
    }

    private TargetFile createTargetFile(
        final Module module,
        String constructorName,
        final String targetFilePath,
        String templateFileName)
    {
        final String[] pair = templateFileName.split(TEMPLATE_NAME_SEPARATOR);
        if (pair.length == 2)
        {
            constructorName = pair[0];
            templateFileName = pair[1];
        }

        final TargetFile targetFile = new TargetFile(targetFilePath, templateFileName);
        final Optional<TemplateFile> templateFile = findTemplateFile(
            module, constructorName, templateFileName + STG_EXT);

        templateFile.ifPresent(targetFile::setTemplateFile);

        return targetFile;
    }

    private Optional<TemplateFile> findTemplateFile(
        final Module module,
        final String constructorName,
        final String templateFileName)
    {
        final Optional<TemplateFile> templateFile = templateRepository.findTemplate(
            module.getName(),
            constructorName,
            templateFileName);

        if (templateFile.isPresent())
        {
            return templateFile;
        }
        else
        {
            for (final Module importedModule: module.getImportedModules())
            {
                final Optional<TemplateFile> importedTemplateFile = templateRepository.findTemplate(
                    importedModule.getName(),
                    constructorName,
                    templateFileName);

                if (importedTemplateFile.isPresent())
                {
                    return importedTemplateFile;
                }
            }
        }

        return templateFile;
    }
}

