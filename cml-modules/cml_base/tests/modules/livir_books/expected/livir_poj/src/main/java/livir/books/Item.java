package livir.books;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public class Item
{
    private final Book book;
    private final int quantity;

    public Item(Book book, int quantity)
    {
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook()
    {
        return this.book;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public String toString()
    {
        return new StringBuilder(Item.class.getSimpleName())
                   .append('(')
                   .append("book=").append(String.format("\"%s\"", getBook())).append(", ")
                   .append("quantity=").append(String.format("\"%s\"", getQuantity()))
                   .append(')')
                   .toString();
    }
}