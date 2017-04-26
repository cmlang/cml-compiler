package cml.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static org.apache.commons.io.FileUtils.forceMkdir;

public interface FileSystem
{
    Optional<Directory> findDirectory(String path);
    Optional<SourceFile> findSourceFile(Directory directory, String name);

    String extractParentPath(String path);
    String extractName(String path);

    void createFile(String path, String content);
    void cleanDirectory(Directory directory);

    static FileSystem create()
    {
        return new FileSystemImpl();
    }
}

class FileSystemImpl implements FileSystem
{
    private static final String EXCEPTION_FILE_CREATION_FAILED = "Unexpected exception. File should have been created: ";
    private static final String EXCEPTION_DIRECTORY_DELETION_FAILED = "Unexpected exception. Dir should have been deleted: ";

    @Override
    public Optional<Directory> findDirectory(final String path)
    {
        final File file = getCanonicalFile(path);
        final Directory directory = file.isDirectory() ? new Directory(path) : null;
        return Optional.ofNullable(directory);
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
