package cml.language;

import static java.lang.String.format;

class ModelLoadingException extends RuntimeException
{
    ModelLoadingException(String message, Object... args)
    {
        super(format(message, args));
    }
}
