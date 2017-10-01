package livir.books;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Book extends Product
{
    private final String isbn;
    private final String title;
    private final String name;

    public Book()
    {
        this(new BigDecimal("0.00"), "Not Provided", "No Title", "");
    }

    public Book(BigDecimal price, String isbn, String title, String name)
    {
        super(name, price);
        this.isbn = isbn;
        this.title = title;
        this.name = name;
    }

    public String getIsbn()
    {
        return this.isbn;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getName()
    {
        return this.name;
    }

    public String toString()
    {
        return new StringBuilder(Book.class.getSimpleName())
                   .append('(')
                   .append("isbn=").append(String.format("\"%s\"", this.getIsbn())).append(", ")
                   .append("title=").append(String.format("\"%s\"", this.getTitle())).append(", ")
                   .append("name=").append(String.format("\"%s\"", this.getName())).append(", ")
                   .append("price=").append(String.format("\"%s\"", this.getPrice()))
                   .append(')')
                   .toString();
    }
}