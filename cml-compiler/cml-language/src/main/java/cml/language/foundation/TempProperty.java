package cml.language.foundation;

import cml.language.features.Association;
import cml.language.features.TempConcept;
import cml.language.generated.*;
import cml.language.types.NamedType;
import cml.language.types.TempType;
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
import static org.jooq.lambda.Seq.seq;

public interface TempProperty extends Property
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

    Optional<TempType> getDeclaredType();

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

    default TempConcept getConcept()
    {
        assert getParent().isPresent();
        assert getParent().get() instanceof TempConcept;

        return (TempConcept) getParent().get();
    }

    default Optional<Association> getAssociation()
    {
        return seq(getModel()).flatMap(m -> seq(((TempModel) m).getAssociations()))
                         .filter(assoc -> assoc.getAssociationEnds()
                                               .stream()
                                               .anyMatch(end -> end.getProperty().isPresent() && end.getProperty().get() == this))
                         .findFirst();
    }

    static TempProperty create(String name, @Nullable NamedType type)
    {
        return create(name, type, null, false, null);
    }

    @SuppressWarnings("unused")
    static TempProperty create(String name, @Nullable Expression value)
    {
        return create(name, null, value, false, null);
    }

    static TempProperty create(String name, @Nullable TempType type, @Nullable Expression value)
    {
        return create(name, type, value, false, null);
    }

    static TempProperty create(String name, @Nullable TempType type, @Nullable Expression value, boolean derived, Location location)
    {
        return new PropertyImpl(name, type, value, derived, location);
    }

    static InvariantValidator<TempProperty> invariantValidator()
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

class PropertyImpl implements TempProperty
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    private boolean typeRequired;
    private boolean typeAllowed;

    private final @Nullable TempType type;
    private final @Nullable Expression value;
    private final boolean derived;

    PropertyImpl(String name, @Nullable TempType type, @Nullable Expression value, boolean derived, Location location)
    {
        modelElement = ModelElement.extendModelElement(this, null, location);
        namedElement = NamedElement.extendNamedElement(this, modelElement, name);
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
    public Optional<TempType> getDeclaredType()
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
    public Optional<Model> getModel()
    {
        return modelElement.getModel();
    }

    @Override
    public Optional<Module> getModule()
    {
        return modelElement.getModule();
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

class GeneralizationCompatibleRedefinition implements Invariant<TempProperty>
{
    @Override
    public boolean evaluate(TempProperty self)
    {
        return getRedefinedProperties(self).allMatch(p -> isAssignableFrom(p.getType(), self.getType()));
    }

    @Override
    public Diagnostic createDiagnostic(TempProperty self)
    {
        final List<TempProperty> conflictingProperties = getRedefinedProperties(self)
                                                        .filter(p -> !isAssignableFrom(p.getType(), self.getType()))
                                                        .collect(Collectors.toList());

        return new Diagnostic("generalization_compatible_redefinition", self, conflictingProperties);
    }

    private Stream<TempProperty> getRedefinedProperties(TempProperty self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof TempConcept)
        {
            final TempConcept concept = (TempConcept) self.getParent().get();

            return concept.getInheritedProperties().stream()
                          .filter(p -> p.getName().equals(self.getName()));
        }
        else
        {
            return Stream.empty();
        }
    }
}

class AbstractPropertyInAbstractConcept implements Invariant<TempProperty>
{
    @Override
    public boolean evaluate(TempProperty self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof TempConcept)
        {
            final TempConcept concept = (TempConcept) self.getParent().get();

            return self.isConcrete() || concept.isAbstract();
        }
        else
        {
            return self.isConcrete();
        }
    }

    @Override
    public Diagnostic createDiagnostic(TempProperty self)
    {
        return new Diagnostic("abstract_property_in_abstract_concept", self, emptyList());
    }
}

class UniquePropertyName implements Invariant<TempProperty>
{
    @Override
    public boolean evaluate(TempProperty self)
    {
        return getConflictingProperties(self).count() == 0;
    }

    @Override
    public Diagnostic createDiagnostic(TempProperty self)
    {
        final List<TempProperty> participants = getConflictingProperties(self).collect(Collectors.toList());

        return new Diagnostic("unique_property_name", self, participants);
    }

    private Stream<TempProperty> getConflictingProperties(TempProperty self)
    {
        if (self.getParent().isPresent() && self.getParent().get() instanceof TempConcept)
        {
            final TempConcept concept = (TempConcept) self.getParent().get();

            return concept.getProperties()
                          .stream()
                          .map(p -> (TempProperty)p)
                          .filter(p -> p != self && p.getName().equals(self.getName()));
        }
        else
        {
            return Stream.empty();
        }
    }
}

class PropertyTypeSpecifiedOrInferred implements Invariant<TempProperty>
{
    @Override
    public boolean evaluate(TempProperty self)
    {
        return self.getType().isDefined();
    }

    @Override
    public Diagnostic createDiagnostic(TempProperty self)
    {
        return new Diagnostic(
            "property_type_specified_or_inferred",
            self,
            self.getType().getErrorMessage().orElse(null));
    }

}

class PropertyTypeAssignableFromExpressionType implements Invariant<TempProperty>
{
    @Override
    public boolean evaluate(TempProperty self)
    {
        return !(self.getDeclaredType().isPresent() && self.getValue().isPresent()) ||
               isAssignableFrom(self.getDeclaredType().get(), self.getValue().get().getType());
    }

    @Override
    public Diagnostic createDiagnostic(TempProperty self)
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
