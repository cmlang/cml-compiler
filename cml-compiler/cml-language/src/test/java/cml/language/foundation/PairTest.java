package cml.language.foundation;

import cml.language.types.NamedType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PairTest
{
    @Test
    public void equals_hashCode() throws Exception
    {
        final Property p1 = Property.create("p1", NamedType.create("Integer"));
        final Property p2 = Property.create("p2", NamedType.create("Decimal"));

        final Pair<Property> pair1 = new Pair<>(p1, p2);
        final Pair<Property> pair2 = new Pair<>(p2, p1);
        
        assertEquals(pair1, pair2);
        assertEquals(pair1.hashCode(), pair2.hashCode());
    }
}