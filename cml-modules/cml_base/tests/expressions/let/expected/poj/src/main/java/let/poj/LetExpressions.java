package let.poj;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class LetExpressions
{
    public int getSquare()
    {
        return new Supplier<Integer>() {
                   public Integer get()
                   {
                       final int a = 2;
                       return (a * a);
                   }
               }.get();
    }

    public String toString()
    {
        return new StringBuilder(LetExpressions.class.getSimpleName())
                   .append('(')
                   .append("square=").append(String.format("\"%s\"", this.getSquare()))
                   .append(')')
                   .toString();
    }
}