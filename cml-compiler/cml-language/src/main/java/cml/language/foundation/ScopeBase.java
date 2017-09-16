package cml.language.foundation;

import cml.language.generated.ModelElement;
import cml.language.generated.Scope;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ScopeBase extends ModelElementBase implements Scope
{
    protected final Scope scope;

    public ScopeBase(@Nullable Scope parent, List<ModelElement> members)
    {
        super(parent);

        this.scope = Scope.extendScope(this, modelElement, members);
    }

    @Override
    public List<ModelElement> getMembers()
    {
        return scope.getMembers();
    }
}
