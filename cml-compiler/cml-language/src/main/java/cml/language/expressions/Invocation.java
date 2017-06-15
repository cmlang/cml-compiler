package cml.language.expressions;

import cml.language.features.Macro;
import cml.language.foundation.*;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toMap;
import static org.jooq.lambda.Seq.seq;

public interface Invocation extends Expression, NamedElement
{
    List<Expression> getArguments();

    Optional<Macro> getMacro();
    void setMacro(@Nullable Macro macro);

    default Map<String, Expression> getNamedArguments()
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
    public Type getType()
    {
        if (getMacro().isPresent())
        {
            final Macro macro = getMacro().get();

            assert macro.getParameters().size() == getArguments().size()
                : "Number of arguments in invocation should match the number of parameters in macro.";

            if (macro.getType().isParameter())
            {
                final int paramIndex = macro.getParamIndexOfMatchingType();
                final Type paramType = getArguments().get(paramIndex).getType();
                final Type type = Type.create(paramType.getName(), macro.getType().getCardinality().orElse(null));

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
            return Type.createUndefined("Unable to find macro of invocation: " + getName());
        }
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
