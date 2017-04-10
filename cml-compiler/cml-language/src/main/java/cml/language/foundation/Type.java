package cml.language.foundation;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;

public interface Type extends NamedElement
{
    Collection<String> primitiveTypeNames = unmodifiableCollection(asList(
        "Boolean", "Integer", "Decimal", "String", "Regex", // main primitive types
        "Byte", "Short", "Long", "Float", "Double", "Char" // remaining primitive types
    ));

    String REQUIRED = "required";
    String OPTIONAL = "optional";
    String SET = "set";

    default boolean isPrimitive()
    {
        return primitiveTypeNames.contains(getName());
    }

    Optional<String> getCardinality();

    default String getKind()
    {
        if (getCardinality().isPresent())
        {
            final String cardinality = getCardinality().get();

            if (cardinality.matches("\\?"))
            {
                return OPTIONAL;
            }
            else if (cardinality.matches("(\\*|\\+)"))
            {
                return SET;
            }
        }

        return REQUIRED;
    }

    static Type create(String name, @Nullable String cardinality)
    {
        return new TypeImpl(name, cardinality);
    }
}

class TypeImpl implements Type
{
    private final NamedElement namedElement;
    private final @Nullable String cardinality;

    TypeImpl(String name, @Nullable String cardinality)
    {
        this.namedElement = NamedElement.create(this, name);
        this.cardinality = cardinality;
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return namedElement.getParentScope();
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }

    @Override
    public Optional<String> getCardinality()
    {
        return Optional.ofNullable(cardinality);
    }
}

