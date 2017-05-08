package cml.language.foundation;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public interface Scope extends ModelElement
{
    void addMember(ModelElement member);

    List<ModelElement> getMembers();

    default <T> List<T> getMembers(Class<T> clazz)
    {
        //noinspection unchecked
        return getMembers().stream()
                           .filter(e -> clazz.isAssignableFrom(e.getClass()))
                           .map(e -> (T)e)
                           .collect(toList());
    }

    default <T> Optional<T> getMemberNamed(String name, Class<T> clazz)
    {
        //noinspection unchecked
        return getMembers(NamedElement.class)
                    .stream()
                    .filter(e -> name.equals(e.getName()))
                    .filter(e -> clazz.isAssignableFrom(e.getClass()))
                    .map(e -> (T)e)
                    .findFirst();
    }

    default <T> Optional<T> getElementNamed(String name, Class<T> clazz)
    {
        final Optional<T> member = getMemberNamed(name, clazz);

        if (member.isPresent())
        {
            return member;
        }
        else if (getParentScope().isPresent())
        {
            return getParentScope().get().getElementNamed(name, clazz);
        }

        return Optional.empty();
    }

    default Optional<Type> getTypeOfMemberNamed(String name)
    {
        final Optional<TypedElement> typedElement = getMemberNamed(name, TypedElement.class);

        return typedElement.flatMap(TypedElement::getType);
    }

    default Optional<Type> getTypeOfElementNamed(String name)
    {
        final Optional<TypedElement> typedElement = getElementNamed(name, TypedElement.class);

        return typedElement.flatMap(TypedElement::getType);
    }

    default Optional<Scope> getScopeOfType(Type type)
    {
        return getElementNamed(type.getName(), Scope.class);
    }

    default <T> Optional<T> getParentScope(Class<T> clazz)
    {
        if (getParentScope().isPresent())
        {
            final Scope scope = getParentScope().get();

            if (clazz.isAssignableFrom(scope.getClass()))
            {
                //noinspection unchecked
                return (Optional<T>) getParentScope();
            }
            else
            {
                return scope.getParentScope(clazz);
            }
        }

        return Optional.empty();
    }

    default Type getSelfType()
    {
        assert getParentScope().isPresent(): "Parent scope required in order to determine self's type.";
        
        return getParentScope().get().getSelfType();
    }

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

    void link(Scope scope, ModelElement modelElement)
    {
        final List<ModelElement> modelElementList =
            elements.computeIfAbsent(scope, key -> new ArrayList<>());

        if (!modelElementList.contains(modelElement))
        {
            modelElementList.add(modelElement);
            parentScope.put(modelElement, scope);
        }
    }

    @SuppressWarnings("unused")
    void unlink(Scope scope, ModelElement modelElement)
    {
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

