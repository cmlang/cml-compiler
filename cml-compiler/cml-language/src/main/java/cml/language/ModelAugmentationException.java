package cml.language;

class ModelAugmentationException extends ModelLoadingException
{
    ModelAugmentationException(String message, Object... args)
    {
        super(message, args);
    }
}
