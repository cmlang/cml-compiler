package paths.cmlc;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface SomeConcept
{
    int getValue();

    Bar getBar();

    List<AnotherConcept> getFoos();

    AnotherConcept getOneMorePath();

    static SomeConcept createSomeConcept(int value, Bar bar, List<AnotherConcept> foos, AnotherConcept oneMorePath)
    {
        return new SomeConceptImpl(null, value, bar, foos, oneMorePath);
    }

    static SomeConcept extendSomeConcept(@Nullable SomeConcept actual_self, int value, Bar bar, List<AnotherConcept> foos, AnotherConcept oneMorePath)
    {
        return new SomeConceptImpl(actual_self, value, bar, foos, oneMorePath);
    }
}

class SomeConceptImpl implements SomeConcept
{
    private final @Nullable SomeConcept actual_self;

    private final int value;
    private final Bar bar;
    private final List<AnotherConcept> foos;
    private final AnotherConcept oneMorePath;

    SomeConceptImpl(@Nullable SomeConcept actual_self, int value, Bar bar, List<AnotherConcept> foos, AnotherConcept oneMorePath)
    {
        this.actual_self = actual_self == null ? this : actual_self;

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
                   .append("value=").append(String.format("\"%s\"", this.actual_self.getValue())).append(", ")
                   .append("bar=").append(String.format("\"%s\"", this.actual_self.getBar())).append(", ")
                   .append("oneMorePath=").append(String.format("\"%s\"", this.actual_self.getOneMorePath()))
                   .append(')')
                   .toString();
    }
}