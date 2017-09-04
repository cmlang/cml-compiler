package cml.language.functions;

import cml.language.expressions.Invocation;
import cml.language.expressions.Lambda;
import cml.language.foundation.ModelElement;
import cml.language.types.NamedType;

import java.util.Optional;

import static cml.language.functions.ScopeFunctions.memberNamed;
import static java.lang.String.format;
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

    public static String diagnosticIdentificationOf(ModelElement modelElement)
    {
        if (modelElement instanceof Invocation)
        {
            final Invocation invocation = (Invocation) modelElement;

            return format("%s -> %s", invocation, invocation.getType());
        }
        else if (modelElement instanceof Lambda)
        {
            final Lambda lambda = (Lambda) modelElement;

            return lambda + " - inferred result type: " + lambda.getMatchingResultType();
        }
        else if (modelElement instanceof NamedType)
        {
            final NamedType namedType = (NamedType) modelElement;

            if (namedType.getErrorMessage().isPresent())
            {
                return namedType + " - " + namedType.getErrorMessage().get();
            }
        }

        return modelElement.toString();
    }

}
