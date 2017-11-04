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

public class Product
{
    private final String name;
    private final BigDecimal price;

    public Product(String name)
    {
        this(name, new BigDecimal("0.00"));
    }

    public Product(String name, BigDecimal price)
    {
        this.name = name;
        this.price = price;
    }

    public String getName()
    {
        return this.name;
    }

    public BigDecimal getPrice()
    {
        return this.price;
    }

    public String toString()
    {
        return new StringBuilder(Product.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", this.getName())).append(", ")
                   .append("price=").append(String.format("\"%s\"", this.getPrice()))
                   .append(')')
                   .toString();
    }
}