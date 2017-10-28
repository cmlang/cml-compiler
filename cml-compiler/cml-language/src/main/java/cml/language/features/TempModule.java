package cml.language.features;

import cml.language.foundation.TempModel;
import cml.language.generated.*;

import java.util.List;
import java.util.Optional;

import static cml.language.generated.Element.extendElement;
import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.Module.extendModule;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public interface TempModule extends NamedElement, Scope, Module
{
    default List<TempModule> getImportedModules()
    {
        return getImports().stream()
                           .map(Import::getImportedModule)
                           .map(m -> (TempModule)m)
                           .collect(toList());
    }

    default List<TempConcept> getImportedConcepts()
    {
        return getImportedModules()
                    .stream()
                    .flatMap(m -> m.getConcepts().stream())
                    .map(c -> (TempConcept)c)
                    .collect(toList());
    }

    default List<TempConcept> getAllConcepts()
    {
        return concat(
            getConcepts().stream(),
            getImportedConcepts().stream())
            .map(c -> (TempConcept)c)
            .collect(toList());
    }

    default List<Template> getTemplates()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Template)
                           .map(e -> (Template)e)
                           .collect(toList());
    }

    default List<Function> getFunctions()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Function)
                           .map(e -> (Function)e)
                           .collect(toList());
    }

    default List<Function> getDefinedFunctions()
    {
        return getMembers().stream()
                           .filter(e -> e instanceof Function)
                           .map(e -> (Function)e)
                           .filter(f -> f.getExpression().isPresent())
                           .collect(toList());
    }

    static TempModule create(TempModel model, String name)
    {
        return new TempModuleImpl(model, name);
    }
}

class TempModuleImpl implements TempModule
{
    private final Module module;

    TempModuleImpl(TempModel model, String name)
    {
        final Element element = extendElement(this);
        final ModelElement modelElement = extendModelElement(this, element, model, null);
        final NamedElement namedElement = extendNamedElement(this, element, modelElement, name);
        final Scope scope = extendScope(this, element, modelElement, emptyList());

        this.module = extendModule(this, element, modelElement, namedElement, scope);
    }

    @Override
    public Optional<Location> getLocation()
    {
        return module.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return module.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return module.getModel();
    }

    @Override
    public String getName()
    {
        return module.getName();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return module.getMembers();
    }

    @Override
    public Optional<Module> getModule()
    {
        return module.getModule();
    }

    @Override
    public List<Import> getImports()
    {
        return module.getImports();
    }

    @Override
    public List<Task> getTasks()
    {
        return module.getTasks();
    }

    @Override
    public List<Concept> getConcepts()
    {
        return module.getConcepts();
    }

    @Override
    public List<Association> getAssociations()
    {
        return module.getAssociations();
    }

    @Override
    public String getDiagnosticId()
    {
        return module.getDiagnosticId();
    }
}
