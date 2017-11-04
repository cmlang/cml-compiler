package functions.poj;

import java.util.*;
import java.util.function.*;
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
    private final @Nullable Item subItem;

    public Item(int size, @Nullable Item subItem)
    {
        this.size = size;
        this.subItem = subItem;
    }

    public int getSize()
    {
        return this.size;
    }

    public Optional<Item> getSubItem()
    {
        return Optional.ofNullable(this.subItem);
    }

    public String toString()
    {
        return new StringBuilder(Item.class.getSimpleName())
                   .append('(')
                   .append("size=").append(String.format("\"%s\"", this.getSize())).append(", ")
                   .append("subItem=").append(this.getSubItem().isPresent() ? String.format("\"%s\"", this.getSubItem()) : "not present")
                   .append(')')
                   .toString();
    }
}