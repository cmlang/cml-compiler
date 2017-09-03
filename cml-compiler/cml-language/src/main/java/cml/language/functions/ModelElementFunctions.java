package cml.language.functions;

import cml.language.foundation.ModelElement;

import java.util.Optional;

import static cml.language.functions.ScopeFunctions.memberNamed;
import static java.util.Optional.empty;

public class ModelElementFunctions
{
    public static <T> Optional<T> siblingNamed(String name, ModelElement element, Class<T> clazz)
    {
        if (element.getParentScope().isPresent())
        {
            return memberNamed(name, element.getParentScope().get(), clazz);
        }
        else
        {
            return empty();
        }
    }
}
