package cml.io;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ModuleManager
{
    void clearBaseDirs();
    void addBaseDir(String path);

    Optional<Directory> findModuleDir(String moduleName);

    Optional<URL> findTemplateFile(String path);

    String getModuleName(String path);
    String getModuleRelativePath(String path);
    String getModulePath(String moduleName, String relativePath);

    static ModuleManager create(Console console, FileSystem fileSystem)
    {
        return new ModuleManagerImpl(console, fileSystem);
    }
}

class ModuleManagerImpl implements ModuleManager
{
    private static final String MODULE_BASE_DIR_NOT_FOUND = "Module base dir not found: %s";
    private static final String UNABLE_TO_FIND_TEMPLATES_DIR = "Unable to find templates dir for module: %s";
    private static final String UNABLE_TO_FIND_MODULE = "Unable to find module: %s";

    private static final String MODULE_NAME_SEPARATOR = ":";
    private static final String TEMPLATES_DIR = "templates";

    private final List<Directory> baseDirList = new ArrayList<>();

    private final Console console;
    private final FileSystem fileSystem;

    ModuleManagerImpl(Console console, FileSystem fileSystem)
    {
        this.console = console;
        this.fileSystem = fileSystem;
    }

    @Override
    public void clearBaseDirs()
    {
        baseDirList.clear();
    }

    @Override
    public void addBaseDir(String path)
    {
        final Optional<Directory> baseDir = fileSystem.findDirectory(path);

        if (baseDir.isPresent())
        {
            baseDirList.add(baseDir.get());
        }
        else
        {
            console.error(MODULE_BASE_DIR_NOT_FOUND, path);
        }
    }

    @Override
    public Optional<Directory> findModuleDir(String moduleName)
    {
        for (Directory baseDir: baseDirList)
        {
            final Optional<Directory> moduleDir = fileSystem.findDirectory(baseDir, moduleName);

            if (moduleDir.isPresent())
            {
                return moduleDir;
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<URL> findTemplateFile(String path)
    {
        final Optional<Directory> moduleDir = findModuleDir(getModuleName(path));

        if (moduleDir.isPresent())
        {
            final Optional<Directory> templatesDir = fileSystem.findDirectory(moduleDir.get(), TEMPLATES_DIR);

            if (templatesDir.isPresent())
            {
                return fileSystem.getURL(templatesDir.get(), getModuleRelativePath(path));
            }
            else
            {
                console.error(UNABLE_TO_FIND_TEMPLATES_DIR, getModuleName(path));
            }
        }
        else
        {
            console.error(UNABLE_TO_FIND_MODULE, getModuleName(path));
        }

        return Optional.empty();
    }

    @Override
    public String getModuleName(String path)
    {
        final String[] pair = path.split(MODULE_NAME_SEPARATOR);

        return pair.length == 2 ? pair[0] : "unspecified_module";
    }

    @Override
    public String getModuleRelativePath(String path)
    {
        final String[] pair = path.split(MODULE_NAME_SEPARATOR);

        return pair.length == 2 ? pair[1] : path;
    }

    @Override
    public String getModulePath(String moduleName, String relativePath)
    {
        if (!relativePath.startsWith(File.separator))
        {
            relativePath = File.separator + relativePath;
        }

        return moduleName + MODULE_NAME_SEPARATOR + relativePath;
    }
}
