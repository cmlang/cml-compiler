package cml.language.foundation;

import cml.language.generated.NamedElement;
import cml.language.generated.Scope;
import org.jetbrains.annotations.Nullable;

import static cml.language.generated.Element.extendElement;
import static cml.language.generated.NamedElement.extendNamedElement;

public abstract class NamedElementBase extends ModelElementBase implements NamedElement
{
    private final NamedElement namedElement;

    public NamedElementBase(String name)
    {
        this(null, name);
    }

    public NamedElementBase(@Nullable Scope parent, String name)
    {
        super(parent);

        namedElement = extendNamedElement(this, extendElement(this), modelElement, name);
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }
}
