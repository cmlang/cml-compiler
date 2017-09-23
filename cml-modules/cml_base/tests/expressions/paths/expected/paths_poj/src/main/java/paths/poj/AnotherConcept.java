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

    public AnotherConcept(BigDecimal etc)
    {
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
                   .append("etc=").append(String.format("\"%s\"", getEtc()))
                   .append(')')
                   .toString();
    }
}