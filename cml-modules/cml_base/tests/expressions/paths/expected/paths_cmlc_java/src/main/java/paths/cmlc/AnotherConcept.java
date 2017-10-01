package paths.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface AnotherConcept
{
    BigDecimal getEtc();

    static AnotherConcept createAnotherConcept(BigDecimal etc)
    {
        return new AnotherConceptImpl(null, etc);
    }

    static AnotherConcept extendAnotherConcept(@Nullable AnotherConcept actual_self, BigDecimal etc)
    {
        return new AnotherConceptImpl(actual_self, etc);
    }
}

class AnotherConceptImpl implements AnotherConcept
{
    private final @Nullable AnotherConcept actual_self;

    private final BigDecimal etc;

    AnotherConceptImpl(@Nullable AnotherConcept actual_self, BigDecimal etc)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.etc = etc;
    }

    public BigDecimal getEtc()
    {
        return this.etc;
    }

    public String toString()
    {
        return new StringBuilder(AnotherConcept.class.getSimpleName())
                   .append('(')
                   .append("etc=").append(String.format("\"%s\"", this.actual_self.getEtc()))
                   .append(')')
                   .toString();
    }
}