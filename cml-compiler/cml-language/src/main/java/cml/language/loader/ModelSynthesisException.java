package cml.language.loader;

class ModelSynthesisException extends ModelLoadingException
{
    ModelSynthesisException(String message, Object... args)
    {
        super(message, args);
    }
}
