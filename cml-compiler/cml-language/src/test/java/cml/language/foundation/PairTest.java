package cml.language.foundation;

import cml.language.types.TempNamedType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PairTest
{
    @Test
    public void equals_hashCode() throws Exception
    {
        final TempProperty p1 = TempProperty.create("p1", TempNamedType.create("Integer"));
        final TempProperty p2 = TempProperty.create("p2", TempNamedType.create("Decimal"));

        final Pair<TempProperty> pair1 = new Pair<>(p1, p2);
        final Pair<TempProperty> pair2 = new Pair<>(p2, p1);
        
        assertEquals(pair1, pair2);
        assertEquals(pair1.hashCode(), pair2.hashCode());
    }
}