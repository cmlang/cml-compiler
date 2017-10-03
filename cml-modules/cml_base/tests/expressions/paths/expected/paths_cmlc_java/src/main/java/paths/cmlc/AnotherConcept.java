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

    boolean isFlag();

    static AnotherConcept createAnotherConcept(BigDecimal etc)
    {
        return createAnotherConcept(etc, true);
    }

    static AnotherConcept createAnotherConcept(BigDecimal etc, boolean flag)
    {
        return new AnotherConceptImpl(null, etc, flag);
    }

    static AnotherConcept extendAnotherConcept(@Nullable AnotherConcept actual_self, BigDecimal etc, boolean flag)
    {
        return new AnotherConceptImpl(actual_self, etc, flag);
    }
}

class AnotherConceptImpl implements AnotherConcept
{
    private final @Nullable AnotherConcept actual_self;

    private final BigDecimal etc;
    private final boolean flag;

    AnotherConceptImpl(@Nullable AnotherConcept actual_self, BigDecimal etc, boolean flag)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.etc = etc;
        this.flag = flag;
    }

    public BigDecimal getEtc()
    {
        return this.etc;
    }

    public boolean isFlag()
    {
        return this.flag;
    }

    public String toString()
    {
        return new StringBuilder(AnotherConcept.class.getSimpleName())
                   .append('(')
                   .append("etc=").append(String.format("\"%s\"", this.actual_self.getEtc())).append(", ")
                   .append("flag=").append(String.format("\"%s\"", this.actual_self.isFlag()))
                   .append(')')
                   .toString();
    }
}