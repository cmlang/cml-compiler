package cml.language.functions;

import cml.language.expressions.Invocation;
import cml.language.expressions.Lambda;
import cml.language.features.Concept;
import cml.language.features.Import;
import cml.language.features.Module;
import cml.language.foundation.ModelElement;
import cml.language.types.NamedType;

import java.util.Optional;

import static cml.language.functions.ScopeFunctions.memberNamed;
import static java.lang.String.format;
import static java.util.Optional.empty;

@SuppressWarnings("WeakerAccess")
public class ModelElementFunctions
{
    public static Optional<Module> moduleOf(ModelElement element)
    {
        if (element instanceof Import)
        {
            final Import _import = (Import) element;

            return _import.getModule();
        }
        else if (element instanceof Module)
        {
            final Module module = (Module) element;

            return Optional.of(module);
        }
        else
        {
            //noinspection Convert2MethodRef
            return element.getParentScope().flatMap(s -> moduleOf(s));
        }
    }

    public static NamedType selfTypeOf(ModelElement element)
    {
        if (element instanceof Concept)
        {
            final Concept concept = (Concept) element;
            final NamedType namedType = NamedType.create(concept.getName());

            namedType.setConcept(concept);

            return namedType;
        }
        else
        {
            assert element.getParentScope().isPresent(): "Parent scope required in order to determine self's type.";

            return selfTypeOf(element.getParentScope().get());
        }
    }

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

    public static String diagnosticIdentificationOf(ModelElement element)
    {
        if (element instanceof Invocation)
        {
            final Invocation invocation = (Invocation) element;

            return format("%s -> %s", invocation, invocation.getType());
        }
        else if (element instanceof Lambda)
        {
            final Lambda lambda = (Lambda) element;

            return lambda + " - inferred result type: " + lambda.getMatchingResultType();
        }
        else if (element instanceof NamedType)
        {
            final NamedType namedType = (NamedType) element;

            if (namedType.getErrorMessage().isPresent())
            {
                return namedType + " - " + namedType.getErrorMessage().get();
            }
        }

        return element.toString();
    }

}
