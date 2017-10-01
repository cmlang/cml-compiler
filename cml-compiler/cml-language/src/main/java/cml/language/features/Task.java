package cml.language.features;

import cml.language.foundation.TempProperty;
import cml.language.foundation.TempPropertyList;
import cml.language.generated.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static org.jooq.lambda.Seq.seq;

public interface Task extends NamedElement, TempPropertyList
{
    Optional<String> getConstructor();

    static Task create(TempModule module, String name, @Nullable String constructor, List<TempProperty> propertyList, Location location)
    {
        return new TaskImpl(module, name, constructor, propertyList, location);
    }
}

class TaskImpl implements Task
{
    private final NamedElement namedElement;
    private final PropertyList propertyList;

    private @Nullable String constructor;

    TaskImpl(TempModule module, String name, @Nullable String constructor, List<TempProperty> properties, Location location)
    {
        final ModelElement modelElement = extendModelElement(this, module, location);
        this.namedElement = extendNamedElement(this, modelElement, name);

        final Scope scope = extendScope(this, modelElement, seq(properties).map(p -> (ModelElement)p).toList());
        propertyList = PropertyList.extendPropertyList(this, modelElement, scope);

        this.constructor = constructor;
    }

    @Override
    public Optional<Location> getLocation()
    {
        return propertyList.getLocation();
    }

    @Override
    public Optional<Scope> getParent()
    {
        return propertyList.getParent();
    }

    @Override
    public Optional<Model> getModel()
    {
        return propertyList.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return propertyList.getModule();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return propertyList.getMembers();
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public Optional<String> getConstructor()
    {
        return Optional.ofNullable(constructor);
    }

    @Override
    public List<Property> getProperties()
    {
        return propertyList.getProperties();
    }

    @Override
    public List<Property> getDerivedProperties()
    {
        return propertyList.getDerivedProperties();
    }
}

