package cml.templates;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static java.lang.String.format;

public interface TemplateRepository
{
    Optional<TemplateFile> findTemplate(String targetType, String fileName);

    static TemplateRepository create()
    {
        return new TemplateRepositoryImpl();
    }
}

class TemplateRepositoryImpl implements TemplateRepository
{
    private static final String TARGET_TYPE_DIR_PATH = "/targets/%s/%s";

    @Override
    public Optional<TemplateFile> findTemplate(final String targetType, final String fileName)
    {
        final String path = format(TARGET_TYPE_DIR_PATH, targetType, fileName);

        try (InputStream stream = getClass().getResourceAsStream(path))
        {
            return (stream == null) ? Optional.empty() : Optional.of(new TemplateFile(path));
        }
        catch (IOException ignored)
        {
            return Optional.empty();
        }
    }

}

