package cml.templates;

import cml.io.ModuleManager;

import java.net.URL;
import java.util.Optional;

import static java.lang.String.format;

public interface TemplateRepository
{
    Optional<TemplateFile> findTemplate(String targetType, String fileName);

    static TemplateRepository create(ModuleManager moduleManager)
    {
        return new TemplateRepositoryImpl(moduleManager);
    }
}

class TemplateRepositoryImpl implements TemplateRepository
{
    private static final String TARGET_TYPE_DIR_PATH = "cml_base:/targets/%s/%s";

    private final ModuleManager moduleManager;

    TemplateRepositoryImpl(ModuleManager moduleManager)
    {
        this.moduleManager = moduleManager;
    }

    @Override
    public Optional<TemplateFile> findTemplate(final String targetType, final String fileName)
    {
        final String path = format(TARGET_TYPE_DIR_PATH, targetType, fileName);
        final Optional<URL> url = moduleManager.findTemplateFile(path);

        return url.isPresent() ? Optional.of(new TemplateFile(path)) : Optional.empty();
    }

}

