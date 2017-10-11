package paths.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface ExpressionCases
{
    String getFoo();

    SomeConcept getSomePath();

    List<SomeConcept> getSomePathList();

    ExpressionCases getSelfVar();

    String getSingleVar();

    SomeConcept getDerivedSomePath();

    int getPathVar();

    BigDecimal getPathVar2();

    List<BigDecimal> getPathVar3();

    List<Integer> getPathBars();

    List<AnotherConcept> getPathFoos();

    List<SomeConcept> getSortedList();

    Optional<AnotherConcept> getOptProp();

    boolean isOptFlag();

    Optional<SomeConcept> getNoneProp();

    static ExpressionCases createExpressionCases(String foo, SomeConcept somePath, List<SomeConcept> somePathList, @Nullable AnotherConcept optProp)
    {
        return new ExpressionCasesImpl(null, foo, somePath, somePathList, optProp);
    }

    static ExpressionCases extendExpressionCases(@Nullable ExpressionCases actual_self, String foo, SomeConcept somePath, List<SomeConcept> somePathList, @Nullable AnotherConcept optProp)
    {
        return new ExpressionCasesImpl(actual_self, foo, somePath, somePathList, optProp);
    }
}

class ExpressionCasesImpl implements ExpressionCases
{
    private final @Nullable ExpressionCases actual_self;

    private final String foo;
    private final SomeConcept somePath;
    private final List<SomeConcept> somePathList;
    private final @Nullable AnotherConcept optProp;

    ExpressionCasesImpl(@Nullable ExpressionCases actual_self, String foo, SomeConcept somePath, List<SomeConcept> somePathList, @Nullable AnotherConcept optProp)
    {
        this.actual_self = actual_self == null ? this : actual_self;

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
        return this.actual_self;
    }

    public String getSingleVar()
    {
        return this.actual_self.getFoo();
    }

    public SomeConcept getDerivedSomePath()
    {
        return this.actual_self.getSomePath();
    }

    public int getPathVar()
    {
        return this.actual_self.getSomePath().getBar();
    }

    public BigDecimal getPathVar2()
    {
        return this.actual_self.getSomePath().getOneMorePath().getEtc();
    }

    public List<BigDecimal> getPathVar3()
    {
        return seq(this.actual_self.getSomePathList()).flatMap(someConcept -> seq(asList(someConcept.getOneMorePath()))).flatMap(anotherConcept -> seq(asList(anotherConcept.getEtc()))).toList();
    }

    public List<Integer> getPathBars()
    {
        return seq(this.actual_self.getSomePathList()).flatMap(someConcept -> seq(asList(someConcept.getBar()))).toList();
    }

    public List<AnotherConcept> getPathFoos()
    {
        return seq(this.actual_self.getSomePathList()).flatMap(someConcept -> seq(someConcept.getFoos())).toList();
    }

    public List<SomeConcept> getSortedList()
    {
        return seq(this.actual_self.getSomePathList()).sorted((item1, item2) -> (item1.getBar() < item2.getBar()) ? -1 : ((item2.getBar() < item1.getBar()) ? +1 : 0)).toList();
    }

    public boolean isOptFlag()
    {
        return seq(this.actual_self.getOptProp()).flatMap(anotherConcept -> seq(asList(anotherConcept.isFlag()))).findFirst().orElse(false);
    }

    public Optional<SomeConcept> getNoneProp()
    {
        return Optional.empty();
    }

    public String toString()
    {
        return new StringBuilder(ExpressionCases.class.getSimpleName())
                   .append('(')
                   .append("foo=").append(String.format("\"%s\"", this.actual_self.getFoo())).append(", ")
                   .append("somePath=").append(String.format("\"%s\"", this.actual_self.getSomePath())).append(", ")
                   .append("singleVar=").append(String.format("\"%s\"", this.actual_self.getSingleVar())).append(", ")
                   .append("pathVar=").append(String.format("\"%s\"", this.actual_self.getPathVar())).append(", ")
                   .append("pathVar2=").append(String.format("\"%s\"", this.actual_self.getPathVar2())).append(", ")
                   .append("pathVar3=").append(this.actual_self.getPathVar3()).append(", ")
                   .append("pathBars=").append(this.actual_self.getPathBars()).append(", ")
                   .append("optProp=").append(this.actual_self.getOptProp().isPresent() ? String.format("\"%s\"", this.actual_self.getOptProp()) : "not present").append(", ")
                   .append("optFlag=").append(String.format("\"%s\"", this.actual_self.isOptFlag()))
                   .append(')')
                   .toString();
    }
}