package literals.poj;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class LiteralExpressions
{
    private final boolean literalTrueBoolean;
    private final boolean literalFalseBoolean;
    private final String literalStringInit;
    private final int literalIntegerInit;
    private final BigDecimal literalDecimalInit;
    private final BigDecimal literalDecimalInit2;

    public LiteralExpressions()
    {
        this(true, false, "\tSome \"String\"\n", 123, new BigDecimal("123.456"), new BigDecimal(".456"));
    }

    public LiteralExpressions(boolean literalTrueBoolean, boolean literalFalseBoolean, String literalStringInit, int literalIntegerInit, BigDecimal literalDecimalInit, BigDecimal literalDecimalInit2)
    {
        this.literalTrueBoolean = literalTrueBoolean;
        this.literalFalseBoolean = literalFalseBoolean;
        this.literalStringInit = literalStringInit;
        this.literalIntegerInit = literalIntegerInit;
        this.literalDecimalInit = literalDecimalInit;
        this.literalDecimalInit2 = literalDecimalInit2;
    }

    public boolean isLiteralTrueBoolean()
    {
        return this.literalTrueBoolean;
    }

    public boolean isLiteralFalseBoolean()
    {
        return this.literalFalseBoolean;
    }

    public String getLiteralStringInit()
    {
        return this.literalStringInit;
    }

    public int getLiteralIntegerInit()
    {
        return this.literalIntegerInit;
    }

    public BigDecimal getLiteralDecimalInit()
    {
        return this.literalDecimalInit;
    }

    public BigDecimal getLiteralDecimalInit2()
    {
        return this.literalDecimalInit2;
    }

    public boolean isComparedStrings()
    {
        return (Objects.equals(this.getLiteralStringInit(), "another string"));
    }

    public String getStrConcat()
    {
        return (this.getLiteralStringInit() + "another string");
    }

    public String getStrConcat2()
    {
        return ((this.getLiteralStringInit() + Objects.toString(321)) + Objects.toString(this.getLiteralDecimalInit()));
    }

    public String getStrConcat3()
    {
        return (Objects.toString(321) + Objects.toString(this.getLiteralDecimalInit()));
    }

    public String getStrConcat4()
    {
        return (this.getLiteralStringInit() + "another string");
    }

    public String toString()
    {
        return new StringBuilder(LiteralExpressions.class.getSimpleName())
                   .append('(')
                   .append("literalTrueBoolean=").append(String.format("\"%s\"", this.isLiteralTrueBoolean())).append(", ")
                   .append("literalFalseBoolean=").append(String.format("\"%s\"", this.isLiteralFalseBoolean())).append(", ")
                   .append("literalStringInit=").append(String.format("\"%s\"", this.getLiteralStringInit())).append(", ")
                   .append("literalIntegerInit=").append(String.format("\"%s\"", this.getLiteralIntegerInit())).append(", ")
                   .append("literalDecimalInit=").append(String.format("\"%s\"", this.getLiteralDecimalInit())).append(", ")
                   .append("literalDecimalInit2=").append(String.format("\"%s\"", this.getLiteralDecimalInit2())).append(", ")
                   .append("comparedStrings=").append(String.format("\"%s\"", this.isComparedStrings())).append(", ")
                   .append("strConcat=").append(String.format("\"%s\"", this.getStrConcat())).append(", ")
                   .append("strConcat2=").append(String.format("\"%s\"", this.getStrConcat2())).append(", ")
                   .append("strConcat3=").append(String.format("\"%s\"", this.getStrConcat3())).append(", ")
                   .append("strConcat4=").append(String.format("\"%s\"", this.getStrConcat4()))
                   .append(')')
                   .toString();
    }
}