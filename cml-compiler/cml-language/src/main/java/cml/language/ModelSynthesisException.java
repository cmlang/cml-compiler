package cml.language;

class ModelSynthesisException extends ModelLoadingException
{
    ModelSynthesisException(String message, Object... args)
    {
        super(message, args);
    }
}
