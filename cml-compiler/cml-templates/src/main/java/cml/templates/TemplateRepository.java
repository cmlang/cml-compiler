package cml.templates;

import cml.io.ModuleManager;

import java.net.URL;
import java.util.Optional;

import static java.lang.String.format;

public interface TemplateRepository
{
    Optional<TemplateFile> findTemplate(String moduleName, String constructorName, String fileName);

    static TemplateRepository create(ModuleManager moduleManager)
    {
        return new TemplateRepositoryImpl(moduleManager);
    }
}

class TemplateRepositoryImpl implements TemplateRepository
{
    private static final String CONSTRUCTOR_PATH = "%s:/constructors/%s/%s";

    private final ModuleManager moduleManager;

    TemplateRepositoryImpl(ModuleManager moduleManager)
    {
        this.moduleManager = moduleManager;
    }

    @Override
    public Optional<TemplateFile> findTemplate(
        final String moduleName,
        final String constructorName,
        final String templateFileName)
    {
        final String path = format(CONSTRUCTOR_PATH, moduleName, constructorName, templateFileName);
        final Optional<URL> url = moduleManager.findTemplateFile(path);

        return url.isPresent() ? Optional.of(new TemplateFile(moduleName, path)) : Optional.empty();
    }

}

