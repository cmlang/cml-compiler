package cml.language.expressions;

import cml.language.foundation.Location;
import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Join extends Expression
{
    List<JoinVar> getVariables();
    boolean isComplete();
    Path getFirstPath();

    default List<String> getVarNames()
    {
        return getVariables().stream()
                             .map(JoinVar::getName)
                             .collect(Collectors.toList());
    }

    default List<Path> getPaths()
    {
        return getVariables().stream()
                             .map(JoinVar::getPath)
                             .collect(Collectors.toList());
    }

    static Join create(List<JoinVar> variables)
    {
        return new JoinImpl(variables);
    }
}

class JoinImpl implements Join
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final List<JoinVar> variables;

    JoinImpl(List<JoinVar> variables)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

        this.variables = new ArrayList<>(variables);
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
    public List<JoinVar> getVariables()
    {
        return new ArrayList<>(variables);
    }

    @Override
    public boolean isComplete()
    {
        return getVariables().size() > 1;
    }

    @Override
    public Path getFirstPath()
    {
        assert getVariables().size() > 0;

        return getVariables().get(0).getPath();
    }

    @Override
    public String getKind()
    {
        return "join";
    }

    @Override
    public Type getType()
    {
        final String typeList = getVarNames().stream()
                                             .collect(Collectors.joining(","));
        return Type.create("Tuple[" + typeList + "]");
    }

    @Override
    public void addMember(ModelElement member)
    {
        scope.addMember(member);
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }

    @Override
    public Optional<Scope> getParentScope()
    {
        return modelElement.getParentScope();
    }
}
