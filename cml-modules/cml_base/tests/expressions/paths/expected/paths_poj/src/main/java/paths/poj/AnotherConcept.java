package paths.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class AnotherConcept
{
    private final BigDecimal etc;
    private final boolean flag;

    public AnotherConcept(BigDecimal etc)
    {
        this(etc, true);
    }

    public AnotherConcept(BigDecimal etc, boolean flag)
    {
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
                   .append("etc=").append(String.format("\"%s\"", this.getEtc())).append(", ")
                   .append("flag=").append(String.format("\"%s\"", this.isFlag()))
                   .append(')')
                   .toString();
    }
}