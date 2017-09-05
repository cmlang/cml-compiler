package cml.language.foundation;

import cml.language.generated.Location;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

public interface Scope extends ModelElement
{
    void addMember(ModelElement member);

    List<ModelElement> getMembers();

    static Scope create(Scope self, ModelElement modelElement)
    {
        return new ScopeImpl(self, modelElement);
    }
}

class ScopeImpl implements Scope
{
    private final Scope self;
    private final ModelElement modelElement;

    ScopeImpl(Scope self, ModelElement modelElement)
    {
        this.self = self;
        this.modelElement = modelElement;
    }

    @Override
    public Optional<Location> getLocation()
    {
        return modelElement.getLocation();
    }

    @Override
    public void setLocation(@Nullable Location location)
    {
        modelElement.setLocation(location);
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scopeElement.getElements(self);
    }

    @Override
    public void addMember(ModelElement member)
    {
        scopeElement.link(self, member);
    }

    private static ScopeElement scopeElement;

    static void setScopeElement(ScopeElement association)
    {
        assert scopeElement == null;

        scopeElement = association;
    }

    static
    {
        ScopeElement.init(ScopeImpl.class);
    }
}

class ScopeElement
{
    private static final String ATTEMPTING_LINK_TO_NULL = "Attempting to link \"%s\" to null.";
    private static final String ATTEMPTING_UNLINK_TO_NULL = "Attempting to unlink \"%s\" to null.";

    private final Map<Scope, List<ModelElement>> elements = new HashMap<>();
    private final Map<ModelElement, Scope> parentScope = new HashMap<>();

    private static final ScopeElement singleton = new ScopeElement();

    static void init(Object object)
    {
        if (ScopeImpl.class.equals(object))
        {
            ScopeImpl.setScopeElement(singleton);
        }
        else if (ModelElementImpl.class.equals(object))
        {
            ModelElementImpl.setScopeElement(singleton);
        }
    }

    void link(final Scope scope, final ModelElement modelElement)
    {
        assert scope != null: format(ATTEMPTING_LINK_TO_NULL, modelElement);
        assert modelElement != null: format(ATTEMPTING_LINK_TO_NULL, scope);

        final List<ModelElement> modelElementList =
            elements.computeIfAbsent(scope, key -> new ArrayList<>());

        if (!modelElementList.contains(modelElement))
        {
            modelElementList.add(modelElement);
            parentScope.put(modelElement, scope);
        }
    }

    @SuppressWarnings("unused")
    void unlink(final Scope scope, final ModelElement modelElement)
    {
        assert scope != null: format(ATTEMPTING_UNLINK_TO_NULL, modelElement);
        assert modelElement != null: format(ATTEMPTING_UNLINK_TO_NULL, scope);

        final List<ModelElement> modelElementList = elements.get(scope);

        if ((modelElementList != null) && modelElementList.contains(modelElement))
        {
            modelElementList.remove(modelElement);
        }

        parentScope.remove(modelElement, scope);
    }

    List<ModelElement> getElements(Scope scope)
    {
        final List<ModelElement> modelElementList = elements.get(scope);

        return (modelElementList == null) ? emptyList() : modelElementList;
    }

    Optional<Scope> getParentScope(ModelElement modelElement)
    {
        return Optional.ofNullable(parentScope.get(modelElement));
    }
}

