package cml.language.functions;

import cml.language.expressions.Invocation;
import cml.language.expressions.Lambda;
import cml.language.features.Concept;
import cml.language.features.Import;
import cml.language.features.TempModule;
import cml.language.generated.ModelElement;
import cml.language.types.NamedType;

import java.util.Optional;

import static cml.language.functions.ScopeFunctions.memberNamed;
import static java.lang.String.format;
import static java.util.Optional.empty;

@SuppressWarnings("WeakerAccess")
public class ModelElementFunctions
{
    public static Optional<TempModule> moduleOf(ModelElement element)
    {
        if (element instanceof Import)
        {
            final Import _import = (Import) element;

            return _import.getImportedModule();
        }
        else if (element instanceof TempModule)
        {
            final TempModule module = (TempModule) element;

            return Optional.of(module);
        }
        else
        {
            //noinspection Convert2MethodRef
            return element.getParent().flatMap(s -> moduleOf(s));
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
            assert element.getParent().isPresent(): "Parent scope required in order to determine self's type.";

            return selfTypeOf(element.getParent().get());
        }
    }

    public static <T> Optional<T> siblingNamed(String name, ModelElement element, Class<T> clazz)
    {
        if (element.getParent().isPresent())
        {
            return memberNamed(name, element.getParent().get(), clazz);
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
