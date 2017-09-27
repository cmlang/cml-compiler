package cml.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.io.FileUtils.forceMkdir;
import static org.jooq.lambda.Seq.seq;

public interface FileSystem
{
    Optional<Directory> findDirectory(Directory baseDir, String dirName);
    Optional<Directory> findDirectory(String path);

    List<Directory> findSubDirs(Directory baseDir);

    List<SourceFile> findSourceFiles(Directory sourceDir);
    Optional<SourceFile> findSourceFile(Directory directory, String name);

    Optional<URL> getURL(Directory directory, String path);

    String extractParentPath(String path);
    String extractName(String path);

    void createFile(String path, String content);
    void cleanDirectory(Directory directory);

    static FileSystem create(Console console)
    {
        return new FileSystemImpl(console);
    }
}

class FileSystemImpl implements FileSystem
{
    private static final String EXCEPTION_FILE_CREATION_FAILED = "Unexpected exception. File should have been created: ";
    private static final String EXCEPTION_DIRECTORY_DELETION_FAILED = "Unexpected exception. Dir should have been deleted: ";

    private static final String ERROR_BUILDING_FILE_URL = "Error building file URL: %s";

    private static final String FILE_URL_PREFIX = "file://";
    private static final String CML_FILE_EXT = "cml";

    private final Console console;

    FileSystemImpl(Console console)
    {
        this.console = console;
    }

    @Override
    public Optional<Directory> findDirectory(Directory baseDir, String dirName)
    {
        return findDirectory(baseDir.getPath() + File.separator + dirName);
    }

    @Override
    public List<Directory> findSubDirs(Directory baseDir)
    {
        final String canonicalPath = getCanonicalFile(baseDir.getPath()).getPath();
        final File[] subDirs = new File(canonicalPath).listFiles(File::isDirectory);
        final List<File> subDirList = asList(subDirs == null ? new File[0] : subDirs);

        return seq(subDirList).map(subDir -> findDirectory(subDir.getPath()))
                              .filter(Optional::isPresent)
                              .map(Optional::get)
                              .toList();
    }

    @Override
    public Optional<Directory> findDirectory(final String path)
    {
        final File file = getCanonicalFile(path);
        final Directory directory = file.isDirectory() ? new Directory(path) : null;
        return Optional.ofNullable(directory);
    }

    @Override
    public Optional<URL> getURL(Directory baseDir, String path)
    {
        if (!path.startsWith(File.separator))
        {
            path = File.separator + path;
        }

        final File file = getCanonicalFile(baseDir.getPath() + path);

        if (file.exists())
        {
            try
            {
                final URL url = new URL(FILE_URL_PREFIX + file.getPath());

                return Optional.of(url);
            }
            catch (MalformedURLException exception)
            {
                console.error(ERROR_BUILDING_FILE_URL, exception.getMessage());
            }
        }

        return Optional.empty();
    }

    @Override
    public List<SourceFile> findSourceFiles(Directory sourceDir)
    {
        final String canonicalPath = getCanonicalFile(sourceDir.getPath()).getPath();
        final File[] files = new File(canonicalPath).listFiles(File::isFile);
        final List<File> fileList = asList(files == null ? new File[0] : files);

        final List<SourceFile> sourceFiles = seq(fileList).map(file -> findSourceFile(sourceDir, file.getName()))
                                                          .filter(Optional::isPresent)
                                                          .map(Optional::get)
                                                          .filter(file -> file.getExtension().equals(CML_FILE_EXT))
                                                          .toList();

        final List<SourceFile> all = new ArrayList<>(sourceFiles);
        for (Directory subDir: findSubDirs(sourceDir))
        {
            all.addAll(findSourceFiles(subDir));
        }

        sort(all);

        return unmodifiableList(all);
    }

    @Override
    public Optional<SourceFile> findSourceFile(final Directory directory, final String name)
    {
        final File file = getCanonicalFile(directory.getPath() + File.separator + name);
        final SourceFile sourceFile = file.isFile() ? new SourceFile(file.getPath()) : null;
        return Optional.ofNullable(sourceFile);
    }

    @Override
    public String extractParentPath(String path)
    {
        final File file = getCanonicalFile(path);
        return file.getParentFile().getPath();
    }

    @Override
    public String extractName(String path)
    {
        final File file = getCanonicalFile(path);
        return file.getName();
    }

    @Override
    public void createFile(final String path, final String content)
    {
        try
        {
            final File file = new File(path);

            if (!file.getParentFile().exists())
            {
                forceMkdir(file.getParentFile());
            }

            try (final PrintWriter output = new PrintWriter(file))
            {
                output.print(content);
            }
        }
        catch (final IOException exception)
        {
            throw new RuntimeException(EXCEPTION_FILE_CREATION_FAILED + path, exception);
        }
    }

    @Override
    public void cleanDirectory(final Directory directory)
    {
        try
        {
            FileUtils.cleanDirectory(new File(directory.getPath()));
        }
        catch (final IOException exception)
        {
            throw new RuntimeException(EXCEPTION_DIRECTORY_DELETION_FAILED + directory.getPath(), exception);
        }
    }

    private static File getCanonicalFile(String path)
    {
        File file = new File(path);
        try
        {
            return file.getCanonicalFile();
        }
        catch (IOException exception)
        {
            return file.getAbsoluteFile();
        }
    }
}
