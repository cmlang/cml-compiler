package cml.language.features;

import cml.language.foundation.Property;

public class PropertyRedef
{
    private final Property property;
    private final boolean redefined;

    public PropertyRedef(final Property property, final boolean redefined)
    {
        this.property = property;
        this.redefined = redefined;
    }

    public Property getProperty()
    {
        return property;
    }

    public boolean isRedefined()
    {
        return redefined;
    }
}
