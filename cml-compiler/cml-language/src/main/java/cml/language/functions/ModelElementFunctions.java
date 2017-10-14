package cml.language.functions;

import cml.language.features.TempConcept;
import cml.language.features.TempModule;
import cml.language.generated.Import;
import cml.language.generated.ModelElement;
import cml.language.types.TempNamedType;

import java.util.Optional;

import static cml.language.functions.ScopeFunctions.memberNamed;
import static java.util.Optional.empty;

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
}
