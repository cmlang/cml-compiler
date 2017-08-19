package cml.language.expressions;

import cml.language.features.Function;
import cml.language.features.FunctionParameter;
import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.NamedElement;
import cml.language.foundation.Scope;
import cml.language.types.NamedType;
import cml.language.types.Type;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.tuple.Tuple2;

import java.util.*;

import static java.util.Collections.*;
import static java.util.stream.Collectors.toMap;
import static org.jooq.lambda.Seq.seq;

public interface Invocation extends Expression, NamedElement
{
    List<Expression> getArguments();
    Map<String, Expression> getNamedArguments();

    default Map<FunctionParameter, Expression> getParameterizedArguments()
    {
        if (getFunction().isPresent())
        {
            return seq(getFunction().get().getParameters())
                .zip(getArguments())
                .collect(toMap(Tuple2::v1, Tuple2::v2));
        }
        else
        {
            return emptyMap();
        }
    }

    Optional<Function> getFunction();
    void setFunction(@Nullable Function function);

    default Type getType()
    {
        if (getFunction().isPresent())
        {
            final Function function = getFunction().get();

            assert function.getParameters().size() == getArguments().size()
                : "Number of arguments in invocation should match the number of parameters in function.";

            if (function.getType().isParameter())
            {
                final int paramIndex = function.getParamIndexOfMatchingType();
                final Type paramType = getArguments().get(paramIndex).getType();
                final Type type = paramType.withCardinality(function.getType().getCardinality().orElse(null));

                paramType.getConcept().ifPresent(type::setConcept);

                return type;
            }
            else
            {
                return function.getType();
            }
        }
        else
        {
            return NamedType.createUndefined("Unable to find function of invocation: " + getName());
        }
    }

    static Invocation create(String name, List<Expression> arguments)
    {
        return new InvocationImpl(name, arguments);
    }

    static Invocation create(String name, LinkedHashMap<String, Expression> namedArguments)
    {
        return new ParameterizedInvocation(name, namedArguments);
    }
}

class InvocationImpl implements Invocation
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    private final List<Expression> arguments;

    private @Nullable Function function;

    InvocationImpl(String name, List<Expression> arguments)
    {
        modelElement = ModelElement.create(this);
        namedElement = NamedElement.create(modelElement, name);
        scope = Scope.create(this, modelElement);

        this.arguments = new ArrayList<>(arguments);
    }

    @Override
    public List<Expression> getArguments()
    {
        return unmodifiableList(arguments);
    }

    @Override
    public Map<String, Expression> getNamedArguments()
    {
        if (getFunction().isPresent())
        {
            return seq(getFunction().get().getParameters())
                .zip(getArguments())
                .collect(toMap(t -> t.v1().getName(), Tuple2::v2));
        }
        else
        {
            return emptyMap();
        }
    }

    @Override
    public Optional<Function> getFunction()
    {
        return Optional.ofNullable(function);
    }

    @Override
    public void setFunction(@Nullable Function function)
    {
        this.function = function;
    }

    @Override
    public String getKind()
    {
        return "invocation";
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
    public void addMember(ModelElement member)
    {
        scope.addMember(member);
    }
}

class ParameterizedInvocation implements Invocation
{
    private final ModelElement modelElement;
    private final NamedElement namedElement;
    private final Scope scope;

    private final LinkedHashMap<String, Expression> namedArguments;

    private @Nullable Function function;

    ParameterizedInvocation(String name, LinkedHashMap<String, Expression> namedArguments)
    {
        modelElement = ModelElement.create(this);
        namedElement = NamedElement.create(modelElement, name);
        scope = Scope.create(this, modelElement);

        this.namedArguments = new LinkedHashMap<>(namedArguments);
    }

    @Override
    public List<Expression> getArguments()
    {
        return new ArrayList<>(namedArguments.values());
    }

    @Override
    public Map<String, Expression> getNamedArguments()
    {
        return unmodifiableMap(namedArguments);
    }

    @Override
    public Optional<Function> getFunction()
    {
        return Optional.ofNullable(function);
    }

    @Override
    public void setFunction(@Nullable Function function)
    {
        this.function = function;
    }

    @Override
    public String getKind()
    {
        return "invocation";
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
    public void addMember(ModelElement member)
    {
        scope.addMember(member);
    }
}
