package cml.language.expressions;

import cml.language.features.Macro;
import cml.language.foundation.*;
import cml.language.types.NamedType;
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

    default Map<Parameter, Expression> getParameterizedArguments()
    {
        if (getMacro().isPresent())
        {
            return seq(getMacro().get().getParameters())
                .zip(getArguments())
                .collect(toMap(Tuple2::v1, Tuple2::v2));
        }
        else
        {
            return emptyMap();
        }
    }

    Optional<Macro> getMacro();
    void setMacro(@Nullable Macro macro);

    default NamedType getType()
    {
        if (getMacro().isPresent())
        {
            final Macro macro = getMacro().get();

            assert macro.getParameters().size() == getArguments().size()
                : "Number of arguments in invocation should match the number of parameters in macro.";

            if (macro.getType().isParameter())
            {
                final int paramIndex = macro.getParamIndexOfMatchingType();
                final NamedType paramType = getArguments().get(paramIndex).getType();
                final NamedType type = NamedType.create(paramType.getName(), macro.getType().getCardinality().orElse(null));

                paramType.getConcept().ifPresent(type::setConcept);

                return type;
            }
            else
            {
                return macro.getType();
            }
        }
        else
        {
            return NamedType.createUndefined("Unable to find macro of invocation: " + getName());
        }
    }

    default Scope getParentScopeOf(Parameter parameter)
    {
        assert getMacro().isPresent(): "Macro should have bene found first.";

        final Macro macro = getMacro().get();

        Scope parentScope = this;

        if (parameter.getParameterScope().isPresent())
        {
            final Parameter param = parameter.getParameterScope().get();
            final int paramIndex = macro.getParameters().indexOf(param);

            if (getArguments().size() > paramIndex)
            {
                parentScope = getArguments().get(paramIndex);
            }
        }

        return parentScope;
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

    private @Nullable Macro macro;

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
        if (getMacro().isPresent())
        {
            return seq(getMacro().get().getParameters())
                .zip(getArguments())
                .collect(toMap(t -> t.v1().getName(), Tuple2::v2));
        }
        else
        {
            return emptyMap();
        }
    }

    @Override
    public Optional<Macro> getMacro()
    {
        return Optional.ofNullable(macro);
    }

    @Override
    public void setMacro(@Nullable Macro macro)
    {
        this.macro = macro;
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

    private @Nullable Macro macro;

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
    public Optional<Macro> getMacro()
    {
        return Optional.ofNullable(macro);
    }

    @Override
    public void setMacro(@Nullable Macro macro)
    {
        this.macro = macro;
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
