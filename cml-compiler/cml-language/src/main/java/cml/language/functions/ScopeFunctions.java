package cml.language.functions;

import cml.language.expressions.LambdaScope;
import cml.language.expressions.Path;
import cml.language.features.Concept;
import cml.language.generated.NamedElement;
import cml.language.generated.Scope;
import cml.language.types.NamedType;
import cml.language.types.Type;
import cml.language.types.TypedElement;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("WeakerAccess")
public class ScopeFunctions
{
    public static <T> List<T> membersOf(Scope scope, Class<T> clazz)
    {
        //noinspection unchecked
        return scope.getMembers()
                    .stream()
                    .filter(e -> clazz.isAssignableFrom(e.getClass()))
                    .map(e -> (T)e)
                    .collect(toList());
    }

    public static <T> Optional<T> memberNamed(String name, Scope scope, Class<T> clazz)
    {
        //noinspection unchecked
        return membersOf(scope, NamedElement.class)
            .stream()
            .filter(e -> name.equals(e.getName()))
            .filter(e -> clazz.isAssignableFrom(e.getClass()))
            .map(e -> (T)e)
            .findFirst();
    }

    public static Optional<Type> typeOfMemberNamed(String name, Scope scope)
    {
        final Optional<TypedElement> typedElement = memberNamed(name, scope, TypedElement.class);

        return typedElement.map(TypedElement::getType);
    }

    public static <T> Optional<T> elementNamed(String name, Scope scope, Class<T> clazz)
    {
        final Optional<T> member = memberNamed(name, scope, clazz);

        if (member.isPresent())
        {
            return member;
        }
        else if (scope.getParent().isPresent())
        {
            return elementNamed(name, scope.getParent().get(), clazz);
        }

        return empty();
    }

    public static Optional<Scope> scopeOfType(Type type, Scope scope)
    {
        if (type instanceof NamedType)
        {
            final NamedType namedType = (NamedType)type;
            return elementNamed(namedType.getName(), scope, Scope.class);
        }

        return empty();
    }

    public static Optional<Type> typeOfVariableNamed(String name, Scope scope)
    {
        if (scope instanceof LambdaScope)
        {
            final LambdaScope lambdaScope = (LambdaScope) scope;
            final Optional<Type> type = ofNullable(lambdaScope.getParameters().get(name));

            if (type.isPresent()) return type;
        }
        else if (scope instanceof Path)
        {
            final Path path = (Path) scope;
            final Optional<Scope> typeScope = scopeOfType(path.getType(), scope);
            final Optional<Type> type = typeScope.flatMap(s -> typeOfVariableNamed(name, s));

            if (type.isPresent()) return type;
        }
        else if (scope instanceof Concept)
        {
            final Optional<Type> memberType = typeOfMemberNamed(name, scope);
            if (memberType.isPresent()) return memberType;

            final Concept concept = (Concept) scope;

            for (Concept ancestor: concept.getDirectAncestors())
            {
                final Optional<Type> type = typeOfVariableNamed(name, ancestor);

                if (type.isPresent()) return type;
            }
        }
        else
        {
            final Optional<Type> memberType = typeOfMemberNamed(name, scope);
            if (memberType.isPresent()) return memberType;
        }

        return scope.getParent().flatMap(s -> typeOfVariableNamed(name, s));
    }
}
