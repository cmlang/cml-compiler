package cml.language.features;

import cml.language.foundation.Property;
import cml.language.foundation.PropertyList;
import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.NamedElement;
import cml.language.generated.Scope;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static cml.language.generated.ModelElement.extendModelElement;
import static cml.language.generated.NamedElement.extendNamedElement;
import static cml.language.generated.Scope.extendScope;
import static org.jooq.lambda.Seq.seq;

public interface Task extends NamedElement, PropertyList
{
    Optional<String> getConstructor();

    static Task create(TempModule module, String name, @Nullable String constructor, List<Property> propertyList, Location location)
    {
        return new TaskImpl(module, name, constructor, propertyList, location);
    }
}

class TaskImpl implements Task
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    private @Nullable String constructor;

    TaskImpl(TempModule module, String name, @Nullable String constructor, List<Property> propertyList, Location location)
    {
        this.modelElement = extendModelElement(this, module, location);
        this.namedElement = extendNamedElement(modelElement, name);
        this.scope = extendScope(this, modelElement, seq(propertyList).map(p -> (ModelElement)p).toList());

        this.constructor = constructor;
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

    @Override
    public Optional<String> getConstructor()
    {
        return Optional.ofNullable(constructor);
    }
}

