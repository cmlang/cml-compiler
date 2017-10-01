package paths.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface SomeConcept
{
    int getBar();

    List<AnotherConcept> getFoos();

    AnotherConcept getOneMorePath();

    static SomeConcept createSomeConcept(int bar, List<AnotherConcept> foos, AnotherConcept oneMorePath)
    {
        return new SomeConceptImpl(null, bar, foos, oneMorePath);
    }

    static SomeConcept extendSomeConcept(@Nullable SomeConcept actual_self, int bar, List<AnotherConcept> foos, AnotherConcept oneMorePath)
    {
        return new SomeConceptImpl(actual_self, bar, foos, oneMorePath);
    }
}

class SomeConceptImpl implements SomeConcept
{
    private final @Nullable SomeConcept actual_self;

    private final int bar;
    private final List<AnotherConcept> foos;
    private final AnotherConcept oneMorePath;

    SomeConceptImpl(@Nullable SomeConcept actual_self, int bar, List<AnotherConcept> foos, AnotherConcept oneMorePath)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.bar = bar;
        this.foos = foos;
        this.oneMorePath = oneMorePath;
    }

    public int getBar()
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
                   .append("bar=").append(String.format("\"%s\"", this.actual_self.getBar())).append(", ")
                   .append("oneMorePath=").append(String.format("\"%s\"", this.actual_self.getOneMorePath()))
                   .append(')')
                   .toString();
    }
}