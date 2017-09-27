package cml.language.foundation;

import cml.language.expressions.Expression;
import cml.language.features.Association;
import cml.language.features.Concept;
import cml.language.generated.Location;
import cml.language.generated.ModelElement;
import cml.language.generated.NamedElement;
import cml.language.generated.Scope;
import cml.language.types.NamedType;
import cml.language.types.Type;
import cml.language.types.TypedElement;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cml.language.functions.TypeFunctions.isAssignableFrom;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public interface Property extends TypedElement, Scope
{
    default boolean isConcrete()
    {
        return !isAbstract();
    }

    default boolean isAbstract()
    {
        return isDerived() && !getValue().isPresent();
    }

    default boolean isInit()
    {
        return getValue().isPresent() && !isDerived();
    }

    default boolean isSlot()
    {
        return !isDerived() && !isAssociationEnd();
    }

    default boolean isAssociationEnd()
    {
        return getAssociation().isPresent();
    }

    Optional<Type> getDeclaredType();

    Optional<Expression> getValue();
    boolean isDerived();

    @SuppressWarnings("unused")
    boolean isTypeRequired();

    @SuppressWarnings("unused")
    void setTypeRequired(boolean typeRequired);

    @SuppressWarnings("unused")
    boolean isTypeAllowed();

    @SuppressWarnings("unused")
    void setTypeAllowed(boolean typeAllowed);

    default Concept getConcept()
    {
        assert getParent().isPresent();
        assert getParent().get() instanceof Concept;

        return (Concept) getParent().get();
    }

    default TempModel getModel()
    {
        return getConcept().getModel();
    }

    default Optional<Association> getAssociation()
    {
        return getModel().getAssociations()
                         .stream()
                         .filter(assoc -> assoc.getAssociationEnds()
                                               .stream()
                                               .anyMatch(end -> end.getProperty().isPresent() && end.getProperty().get() == this))
                         .findFirst();
    }

    static Property create(String name, @Nullable NamedType type)
    {
        return create(name, type, null, false, null);
    }

    @SuppressWarnings("unused")
    static Property create(String name, @Nullable Expression value)
    {
        return create(name, null, value, false, null);
    }

    static Property create(String name, @Nullable Type type, @Nullable Expression value)
    {
        return create(name, type, value, false, null);
    }

    static Property create(String name, @Nullable Type type, @Nullable Expression value, boolean derived, Location location)
    {
        return new PropertyImpl(name, type, value, derived, location);
    }

    static InvariantValidator<Property> invariantValidator()
    {
        return () -> asList(
            new UniquePropertyName(),
            new PropertyTypeSpecifiedOrInferred(),
            new PropertyTypeAssignableFromExpressionType(),
            new GeneralizationCompatibleRedefinition(),
            new AbstractPropertyInAbstractConcept()
        );
    }
}

class PropertyImpl implements Property
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    private boolean typeRequired;
    private boolean typeAllowed;

    private final @Nullable Type type;
    private final @Nullable Expression value;
    private final boolean derived;

    PropertyImpl(String name, @Nullable Type type, @Nullable Expression value, boolean derived, Location location)
    {
        modelElement = ModelElement.extendModelElement(this, null, location);
        namedElement = NamedElement.extendNamedElement(modelElement, name);
        scope = Scope.extendScope(this, modelElement, singletonList(value));

        this.type = type;
        this.value = value;
        this.derived = derived;
    }

    @Override
    public boolean isDerived()
    {
        return derived;
    }

    @Override
    public Optional<Type> getDeclaredType()
    {
        return Optional.ofNullable(type);
    }

    @Override
    public Optional<Expression> getValue()
    {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean isTypeRequired()
    {
        return typeRequired;
    }

    @Override
    public void setTypeRequired(boolean typeRequired)
    {
        this.typeRequired = typeRequired;
    }

    @Override
    public boolean isTypeAllowed()
    {
        return typeAllowed;
    }

    @Override
    public void setTypeAllowed(boolean typeAllowed)
    {
        this.typeAllowed = typeAllowed;
    }

    @Override
    public Type getType()
    {
        if (type == null)
        {
            if (value == null)
            {
                return NamedType.createUndefined("No type or expression defined for property: " + getName());
            }
            else
            {
                return value.getType();
            }
        }
        else
        {
            return type;
        }
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
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
    public String toString()
    {
        if (getParent().isPresent() && getParent().get() instanceof NamedElement)
        {
            final NamedElement namedElement = (NamedElement)getParent().get();

            return format("property %s.%s: %s",  namedElement.getName(), getName(), getType());
        }
        else
        {
            return format("property %s: %s",  getName(), getType());
        }
    }
}

class GeneralizationCompatibleRedefinition implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        return getRedefinedProperties(self).allMatch(p -> p.matchesTypeOf(self));
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        final List<Property> conflictingProperties = getRedefinedProperties(self)
                                                        .filter(p -> !p.matchesTypeOf(self))
                                                        .collect(Collectors.toList());

        return new Diagnostic("generalization_compatible_redefinition", self, conflictingProperties);
    }

    private Stream<Property> getRedefinedProperties(Property self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof Concept)
        {
            final Concept concept = (Concept) self.getParent().get();

            return concept.getInheritedProperties().stream()
                          .filter(p -> p.getName().equals(self.getName()));
        }
        else
        {
            return Stream.empty();
        }
    }
}

class AbstractPropertyInAbstractConcept implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof Concept)
        {
            final Concept concept = (Concept) self.getParent().get();

            return self.isConcrete() || concept.isAbstract();
        }
        else
        {
            return self.isConcrete();
        }
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        return new Diagnostic("abstract_property_in_abstract_concept", self, emptyList());
    }
}

class UniquePropertyName implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        return getConflictingProperties(self).count() == 0;
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        final List<Property> participants = getConflictingProperties(self).collect(Collectors.toList());

        return new Diagnostic("unique_property_name", self, participants);
    }

    private Stream<Property> getConflictingProperties(Property self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof Concept)
        {
            final Concept concept = (Concept) self.getParent().get();

            return concept.getProperties()
                          .stream()
                          .filter(p -> p != self && p.getName().equals(self.getName()));
        }
        else
        {
            return Stream.empty();
        }
    }
}

class PropertyTypeSpecifiedOrInferred implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        return self.getType().isDefined();
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        return new Diagnostic(
            "property_type_specified_or_inferred",
            self,
            self.getType().getErrorMessage().orElse(null));
    }

}

class PropertyTypeAssignableFromExpressionType implements Invariant<Property>
{
    @Override
    public boolean evaluate(Property self)
    {
        return !(self.getDeclaredType().isPresent() && self.getValue().isPresent()) ||
               isAssignableFrom(self.getDeclaredType().get(), self.getValue().get().getType());
    }

    @Override
    public Diagnostic createDiagnostic(Property self)
    {
        assert self.getDeclaredType().isPresent();
        assert self.getValue().isPresent();

        return new Diagnostic(
            "property_type_assignable_from_expression_type",
            self,
            format(
                "Declared type is %s but type inferred from expression is %s.",
                self.getDeclaredType().get(),
                self.getValue().get().getType()));
    }

}
