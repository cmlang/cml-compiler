package cml.language.functions;

import cml.language.expressions.Invocation;
import cml.language.expressions.Lambda;
import cml.language.expressions.Path;
import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.generated.Association;
import cml.language.generated.AssociationEnd;
import cml.language.generated.Import;
import cml.language.generated.ModelElement;
import cml.language.types.TempNamedType;

import java.util.Optional;

import static cml.language.functions.ScopeFunctions.memberNamed;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.joining;

@SuppressWarnings("WeakerAccess")
public class ModelElementFunctions
{
    public static Optional<TempModule> moduleOf(ModelElement element)
    {
        if (element instanceof Import)
        {
            final Import _import = (Import) element;
            final TempModule module = (TempModule) _import.getImportedModule();

            return Optional.of(module);
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

    public static TempNamedType selfTypeOf(ModelElement element)
    {
        if (element instanceof TempConcept)
        {
            final TempConcept concept = (TempConcept) element;
            final TempNamedType namedType = TempNamedType.create(concept.getName());

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
        else if (element instanceof TempNamedType)
        {
            final TempNamedType namedType = (TempNamedType) element;

            if (namedType.getErrorMessage().isPresent())
            {
                return namedType + " - " + namedType.getErrorMessage().get();
            }
        }
        else if (element instanceof AssociationEnd)
        {
            final AssociationEnd associationEnd = (AssociationEnd) element;

            if (associationEnd.getPropertyType().isPresent())
            {
                return format(
                    "association end %s.%s: %s",
                    associationEnd.getConceptName(), associationEnd.getPropertyName(), associationEnd.getPropertyType().get());
            }
            else
            {
                return format("association end %s.%s", associationEnd.getConceptName(), associationEnd.getPropertyName());
            }
        }
        else if (element instanceof Association)
        {
            final Association association = (Association) element;

            return "association " + association.getName();
        }
        else if (element instanceof Path)
        {
            final Path path = (Path) element;

            return path.getNames().stream().collect(joining(".")) + ": " + path.getType();
        }

        return element.toString();
    }

}
