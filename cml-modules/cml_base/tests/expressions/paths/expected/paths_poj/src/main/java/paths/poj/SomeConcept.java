package paths.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class SomeConcept
{
    private final int value;
    private final Bar bar;
    private final List<AnotherConcept> foos;
    private final AnotherConcept oneMorePath;

    public SomeConcept(int value, Bar bar, List<AnotherConcept> foos, AnotherConcept oneMorePath)
    {
        this.value = value;
        this.bar = bar;
        this.foos = foos;
        this.oneMorePath = oneMorePath;
    }

    public int getValue()
    {
        return this.value;
    }

    public Bar getBar()
    {
        return this.bar;
    }

    public List<AnotherConcept> getFoos()
    {
        return Collections.unmodifiableList(this.foos);
    }

    public AnotherConcept getOneMorePath()
    {
        return this.oneMorePath;
    }

    public String toString()
    {
        return new StringBuilder(SomeConcept.class.getSimpleName())
                   .append('(')
                   .append("value=").append(String.format("\"%s\"", this.getValue())).append(", ")
                   .append("bar=").append(String.format("\"%s\"", this.getBar())).append(", ")
                   .append("oneMorePath=").append(String.format("\"%s\"", this.getOneMorePath()))
                   .append(')')
                   .toString();
    }
}