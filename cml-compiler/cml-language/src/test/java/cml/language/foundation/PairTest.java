package cml.language.foundation;

import cml.language.generated.Property;
import cml.language.types.TempNamedType;
import org.junit.Test;

import static cml.language.generated.Property.createProperty;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public class PairTest
{
    @Test
    public void equals_hashCode() throws Exception
    {
        final Property p1 = createProperty( "p1", null, null, emptyList(), false, TempNamedType.create("Integer"), null, null);
        final Property p2 = createProperty( "p2", null, null, emptyList(), false, TempNamedType.create("Decimal"), null, null);

        final Pair<Property> pair1 = new Pair<>(p1, p2);
        final Pair<Property> pair2 = new Pair<>(p2, p1);
        
        assertEquals(pair1, pair2);
        assertEquals(pair1.hashCode(), pair2.hashCode());
    }
}