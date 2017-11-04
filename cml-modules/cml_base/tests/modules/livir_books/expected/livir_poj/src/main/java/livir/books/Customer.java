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

public class Customer
{
    private final String name;

    public Customer(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String toString()
    {
        return new StringBuilder(Customer.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", this.getName()))
                   .append(')')
                   .toString();
    }
}