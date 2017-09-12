package paths.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public class SomeConcept
{
    private final int bar;
    private final AnotherConcept oneMorePath;

    public SomeConcept(int bar, AnotherConcept oneMorePath)
    {
        this.bar = bar;
        this.oneMorePath = oneMorePath;
    }

    public int getBar()
    {
        return this.bar;
    }

    public AnotherConcept getOneMorePath()
    {
        return this.oneMorePath;
    }

    public String toString()
    {
        return new StringBuilder(SomeConcept.class.getSimpleName())
                   .append('(')
                   .append("bar=").append(String.format("\"%s\"", getBar())).append(", ")
                   .append("oneMorePath=").append(String.format("\"%s\"", getOneMorePath()))
                   .append(')')
                   .toString();
    }
}