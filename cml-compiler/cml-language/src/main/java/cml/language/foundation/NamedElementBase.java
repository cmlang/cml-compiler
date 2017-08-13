package cml.language.foundation;

public class NamedElementBase extends ModelElementBase implements NamedElement
{
    private final NamedElement namedElement;

    public NamedElementBase(String name)
    {
        namedElement = NamedElement.create(modelElement, name);
    }

    @Override
    public String getName()
    {
        return namedElement.getName();
    }
}
