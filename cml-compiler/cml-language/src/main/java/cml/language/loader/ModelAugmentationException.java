package cml.language.loader;

class ModelAugmentationException extends ModelLoadingException
{
    ModelAugmentationException(String message, Object... args)
    {
        super(message, args);
    }
}
