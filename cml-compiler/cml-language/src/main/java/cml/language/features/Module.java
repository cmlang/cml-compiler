package cml.language.features;

import cml.language.foundation.Model;
import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.NamedElement;
import cml.language.generated.Scope;

import java.util.List;
import java.util.Optional;

import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public interface Module extends NamedElement, Scope
{
    default Model getModel()
    {
        assert getParent().isPresent();
        assert getParent().get() instanceof Model;

        return (Model) getParent().get();
    }

    default List<Import> getImports()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Import)
                           .map(e -> (Import)e)
                           .collect(toList());
    }

    default List<Module> getImportedModules()
    {
        return getImports().stream()
                           .map(Import::getImportedModule)
                           .filter(Optional::isPresent)
                           .map(Optional::get)
                           .collect(toList());
    }

    default Optional<Module> getImportedModule(final String name)
    {
        for (final Module module: getImportedModules())
        {
            if (module.getName().equals(name))
            {
                return Optional.of(module);
            }
        }

        return Optional.empty();
    }

    default Optional<Module> getSelfOrImportedModule(final String name)
    {
        if (getName().equals(name))
        {
            return Optional.of(this);
        }

        return getImportedModule(name);
    }

    default List<Concept> getConcepts()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Concept)
                           .map(e -> (Concept)e)
                           .collect(toList());
    }

    default List<Concept> getImportedConcepts()
    {
        return getImportedModules()
                    .stream()
                    .flatMap(m -> m.getConcepts().stream())
                    .collect(toList());
    }

    default List<Concept> getAllConcepts()
    {
        return concat(
            getConcepts().stream(),
            getImportedConcepts().stream())
            .collect(toList());
    }

    default Optional<Concept> getConcept(String name)
    {
        return getAllConcepts().stream()
                               .filter(concept -> concept.getName().equals(name))
                               .findFirst();
    }

    default List<Association> getAssociations()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Association)
                           .map(e -> (Association)e)
                           .collect(toList());
    }

    default List<Task> getTasks()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Task)
                           .map(e -> (Task)e)
                           .collect(toList());
    }

    default List<Template> getTemplates()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Template)
                           .map(e -> (Template)e)
                           .collect(toList());
    }

    default List<Template> getImportedTemplates()
    {
        return getImportedModules()
                    .stream()
                    .flatMap(m -> m.getTemplates().stream())
                    .collect(toList());
    }

    default List<Template> getAllTemplates()
    {
        return concat(getTemplates().stream(), getImportedTemplates().stream()).collect(toList());
    }

    default Optional<Template> getTemplate(String name)
    {
        return getAllTemplates().stream()
                                .filter(t -> t.getName().equals(name))
                                .findFirst();
    }

    static Module create(Model model, String name)
    {
        return new ModuleImpl(model, name);
    }
}

class ModuleImpl implements Module
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    ModuleImpl(Model model, String name)
    {
        this.modelElement = extendModelElement(this, model, null);
        this.namedElement = extendNamedElement(modelElement, name);
        this.scope = extendScope(this, modelElement, emptyList());
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return modelElement.getParent();
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }
}
