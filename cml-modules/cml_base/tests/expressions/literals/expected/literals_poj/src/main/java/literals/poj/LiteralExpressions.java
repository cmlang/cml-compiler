package literals.poj;

import java.util.*;
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

    public String toString()
    {
        return new StringBuilder(LiteralExpressions.class.getSimpleName())
                   .append('(')
                   .append("literalTrueBoolean=").append(String.format("\"%s\"", isLiteralTrueBoolean())).append(", ")
                   .append("literalFalseBoolean=").append(String.format("\"%s\"", isLiteralFalseBoolean())).append(", ")
                   .append("literalStringInit=").append(String.format("\"%s\"", getLiteralStringInit())).append(", ")
                   .append("literalIntegerInit=").append(String.format("\"%s\"", getLiteralIntegerInit())).append(", ")
                   .append("literalDecimalInit=").append(String.format("\"%s\"", getLiteralDecimalInit())).append(", ")
                   .append("literalDecimalInit2=").append(String.format("\"%s\"", getLiteralDecimalInit2()))
                   .append(')')
                   .toString();
    }
}