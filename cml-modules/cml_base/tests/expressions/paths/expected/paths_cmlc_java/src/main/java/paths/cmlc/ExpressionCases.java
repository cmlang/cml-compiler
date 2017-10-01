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

    static ExpressionCases createExpressionCases(String foo, SomeConcept somePath, List<SomeConcept> somePathList)
    {
        return new ExpressionCasesImpl(null, foo, somePath, somePathList);
    }

    static ExpressionCases extendExpressionCases(@Nullable ExpressionCases actual_self, String foo, SomeConcept somePath, List<SomeConcept> somePathList)
    {
        return new ExpressionCasesImpl(actual_self, foo, somePath, somePathList);
    }
}

class ExpressionCasesImpl implements ExpressionCases
{
    private final @Nullable ExpressionCases actual_self;

    private final String foo;
    private final SomeConcept somePath;
    private final List<SomeConcept> somePathList;

    ExpressionCasesImpl(@Nullable ExpressionCases actual_self, String foo, SomeConcept somePath, List<SomeConcept> somePathList)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.foo = foo;
        this.somePath = somePath;
        this.somePathList = somePathList;
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
                   .append("pathBars=").append(this.actual_self.getPathBars())
                   .append(')')
                   .toString();
    }
}