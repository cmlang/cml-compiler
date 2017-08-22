package cml.language.loader;

import static java.lang.String.format;

class ModelLoadingException extends RuntimeException
{
    ModelLoadingException(String message, Object... args)
    {
        super(format(message, args));
    }
}
