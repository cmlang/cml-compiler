package cml.language.expressions;

import cml.language.foundation.ModelElement;
import cml.language.foundation.Scope;
import cml.language.foundation.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Join extends Expression
{
    List<String> getVariables();
    List<Path> getPaths();
    boolean isComplete();
    Path getFirstPath();

    static Join create(List<String> variables, List<Path> paths)
    {
        return new JoinImpl(variables, paths);
    }
}

class JoinImpl implements Join
{
    private final ModelElement modelElement;
    private final Scope scope;

    private final List<String> variables;
    private final List<Path> paths;

    JoinImpl(List<String> variables, List<Path> paths)
    {
        modelElement = ModelElement.create(this);
        scope = Scope.create(this, modelElement);

        this.variables = new ArrayList<>(variables);
        this.paths = new ArrayList<>(paths);
    }

    @Override
    public List<String> getVariables()
    {
        return new ArrayList<>(variables);
    }

    @Override
    public List<Path> getPaths()
    {
        return new ArrayList<>(paths);
    }

    @Override
    public boolean isComplete()
    {
        return getPaths().size() > 1;
    }

    @Override
    public Path getFirstPath()
    {
        assert getPaths().size() > 0;

        return getPaths().get(0);
    }

    @Override
    public String getKind()
    {
        return "join";
    }

    @Override
    public Type getType()
    {
        final String typeList = getPaths().stream()
                                          .map(p -> p.getType().getName())
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
