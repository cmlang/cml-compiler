package cml.language.features;

import cml.language.foundation.ScopeBase;
import cml.language.generated.Expression;
import cml.language.generated.Type;
import cml.language.types.TypeParameter;
import org.jetbrains.annotations.Nullable;
import org.jooq.lambda.Seq;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static cml.language.generated.UndefinedType.createUndefinedType;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static org.jooq.lambda.Seq.*;

public class Function extends ScopeBase
{
    private String name;
    private Type type;
    private List<TypeParameter> typeParams;

    public Function(final String name, final Type type, final Stream<FunctionParameter> parameters)
    {
        this(null, name, type, parameters);
    }

    public Function(@Nullable final TempModule module, final String name, final Type type, final Stream<FunctionParameter> parameters)
    {
        this(module, name, type, parameters, empty(), null);
    }

    public Function(@Nullable final TempModule module, final String name, final Type type, final Stream<FunctionParameter> parameters, final Seq<TypeParameter> typeParams, final @Nullable Expression expression)
    {
        super(module, seq(concat(seq(parameters), seq(Optional.ofNullable(expression)))).toList());

        this.name = name;
        this.type = type;
        this.typeParams = typeParams.toList();
    }

    public String getName()
    {
        return name;
    }

    public Type getType()
    {
        return type == null && getExpression().isPresent() ?
            getExpression().get().getType() :
            (type == null ? createUndefinedType("Unable to find type of function: " + getDiagnosticId()) : type);
    }

    public List<FunctionParameter> getParameters()
    {
        return seq(getMembers()).filter(m -> m instanceof FunctionParameter)
                                .cast(FunctionParameter.class)
                                .toList();
    }

    public List<TypeParameter> getTypeParams()
    {
        return unmodifiableList(typeParams);
    }

    public Optional<Expression> getExpression()
    {
        return seq(getMembers()).filter(m -> m instanceof Expression)
                                .cast(Expression.class)
                                .findFirst();
    }

    @Override
    public String getDiagnosticId()
    {
        return format(
            "function %s(%s) -> %s", getName(),
            seq(getParameters()).toString(", "),
            getType().getDiagnosticId());
    }
}
