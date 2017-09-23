package livir.books;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Order
{
    private final Customer customer;
    private final List<Item> items;

    public Order(Customer customer, List<Item> items)
    {
        this.customer = customer;
        this.items = items;
    }

    public Customer getCustomer()
    {
        return this.customer;
    }

    public List<Item> getItems()
    {
        return Collections.unmodifiableList(this.items);
    }

    public String toString()
    {
        return new StringBuilder(Order.class.getSimpleName())
                   .append('(')
                   .append("customer=").append(String.format("\"%s\"", getCustomer()))
                   .append(')')
                   .toString();
    }
}