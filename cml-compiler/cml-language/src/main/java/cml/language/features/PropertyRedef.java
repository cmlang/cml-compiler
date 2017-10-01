package cml.language.features;

import cml.language.foundation.TempProperty;

public class PropertyRedef
{
    private final TempProperty property;
    private final boolean redefined;

    public PropertyRedef(final TempProperty property, final boolean redefined)
    {
        this.property = property;
        this.redefined = redefined;
    }

    public TempProperty getProperty()
    {
        return property;
    }

    public boolean isRedefined()
    {
        return redefined;
    }
}
