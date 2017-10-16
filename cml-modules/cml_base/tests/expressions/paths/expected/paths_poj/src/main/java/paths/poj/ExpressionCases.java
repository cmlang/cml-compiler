package paths.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class ExpressionCases
{
    private final String foo;
    private final SomeConcept somePath;
    private final List<SomeConcept> somePathList;
    private final @Nullable AnotherConcept optProp;

    public ExpressionCases(String foo, SomeConcept somePath, List<SomeConcept> somePathList, @Nullable AnotherConcept optProp)
    {
        this.foo = foo;
        this.somePath = somePath;
        this.somePathList = somePathList;
        this.optProp = optProp;
    }

    public String getFoo()
    {
        return this.foo;
    }

    public SomeConcept getSomePath()
    {
        return this.somePath;
    }

    public List<SomeConcept> getSomePathList()
    {
        return Collections.unmodifiableList(this.somePathList);
    }

    public Optional<AnotherConcept> getOptProp()
    {
        return Optional.ofNullable(this.optProp);
    }

    public ExpressionCases getSelfVar()
    {
        return this;
    }

    public String getSingleVar()
    {
        return this.getFoo();
    }

    public SomeConcept getDerivedSomePath()
    {
        return this.getSomePath();
    }

    public Bar getPathVar()
    {
        return this.getSomePath().getBar();
    }

    public Etc getPathVar2()
    {
        return this.getSomePath().getOneMorePath().getEtc();
    }

    public List<Etc> getPathVar3()
    {
        return seq(this.getSomePathList()).flatMap(someConcept -> seq(asList(someConcept.getOneMorePath()))).flatMap(anotherConcept -> seq(asList(anotherConcept.getEtc()))).toList();
    }

    public List<Bar> getPathBars()
    {
        return seq(this.getSomePathList()).flatMap(someConcept -> seq(asList(someConcept.getBar()))).toList();
    }

    public List<AnotherConcept> getPathFoos()
    {
        return seq(this.getSomePathList()).flatMap(someConcept -> seq(someConcept.getFoos())).toList();
    }

    public List<SomeConcept> getSortedList()
    {
        return seq(this.getSomePathList()).sorted((item1, item2) -> (item1.getValue() < item2.getValue()) ? -1 : ((item2.getValue() < item1.getValue()) ? +1 : 0)).toList();
    }

    public boolean isOptFlag()
    {
        return seq(this.getOptProp()).anyMatch(item1 -> item1.isFlag());
    }

    public Optional<SomeConcept> getNoneProp()
    {
        return Optional.empty();
    }

    public String toString()
    {
        return new StringBuilder(ExpressionCases.class.getSimpleName())
                   .append('(')
                   .append("foo=").append(String.format("\"%s\"", this.getFoo())).append(", ")
                   .append("somePath=").append(String.format("\"%s\"", this.getSomePath())).append(", ")
                   .append("singleVar=").append(String.format("\"%s\"", this.getSingleVar())).append(", ")
                   .append("optProp=").append(this.getOptProp().isPresent() ? String.format("\"%s\"", this.getOptProp()) : "not present").append(", ")
                   .append("optFlag=").append(String.format("\"%s\"", this.isOptFlag()))
                   .append(')')
                   .toString();
    }
}