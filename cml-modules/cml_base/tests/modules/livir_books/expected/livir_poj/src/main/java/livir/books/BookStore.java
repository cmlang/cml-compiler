package livir.books;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class BookStore
{
    private final List<Book> books;
    private final List<Customer> customers;

    public BookStore(List<Book> books, List<Customer> customers)
    {
        this.books = books;
        this.customers = customers;
    }

    public List<Book> getBooks()
    {
        return Collections.unmodifiableList(this.books);
    }

    public List<Customer> getCustomers()
    {
        return Collections.unmodifiableList(this.customers);
    }

    public String toString()
    {
        return new StringBuilder(BookStore.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }
}