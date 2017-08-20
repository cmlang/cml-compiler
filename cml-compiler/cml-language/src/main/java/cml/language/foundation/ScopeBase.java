package cml.language.foundation;

import java.util.List;

public abstract class ScopeBase extends ModelElementBase implements Scope
{
    protected final Scope scope;

    public ScopeBase()
    {
        this.scope = Scope.create(this, modelElement);
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
}
