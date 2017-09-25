package functions.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Item
{
    private final int size;

    public Item(int size)
    {
        this.size = size;
    }

    public int getSize()
    {
        return this.size;
    }

    public String toString()
    {
        return new StringBuilder(Item.class.getSimpleName())
                   .append('(')
                   .append("size=").append(String.format("\"%s\"", getSize()))
                   .append(')')
                   .toString();
    }
}