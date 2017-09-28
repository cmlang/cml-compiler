package cml.language.features;

import cml.language.foundation.TempModel;
import cml.language.generated.*;

import java.util.List;
import java.util.Optional;

import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public interface TempModule extends NamedElement, Scope, Module
{
    default TempModel getModel()
    {
        assert getParent().isPresent();
        assert getParent().get() instanceof TempModel;

        return (TempModel) getParent().get();
    }

    default List<Import> getImports()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Import)
                           .map(e -> (Import)e)
                           .collect(toList());
    }

    default List<TempModule> getImportedModules()
    {
        return getImports().stream()
                           .map(Import::getImportedModule)
                           .map(m -> (TempModule)m)
                           .collect(toList());
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

    static TempModule create(TempModel model, String name)
    {
        return new ModuleImpl(model, name);
    }
}

class ModuleImpl implements TempModule
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;
    private final Module module;

    ModuleImpl(TempModel model, String name)
    {
        this.modelElement = extendModelElement(this, model, null);
        this.namedElement = extendNamedElement(modelElement, name);
        this.scope = extendScope(this, modelElement, emptyList());
        this.module = Module.extendModule(modelElement, namedElement, scope);
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
