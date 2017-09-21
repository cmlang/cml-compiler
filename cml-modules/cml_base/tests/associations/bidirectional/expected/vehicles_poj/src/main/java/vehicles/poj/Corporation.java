package vehicles.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public class Corporation extends Organization
{
    private final boolean stock;
    private final boolean profit;

    public Corporation(String name, List<Employee> employees, List<Vehicle> fleet)
    {
        this(name, employees, fleet, true, true);
    }

    public Corporation(String name, List<Employee> employees, List<Vehicle> fleet, boolean stock, boolean profit)
    {
        super(name, employees, fleet);
        this.stock = stock;
        this.profit = profit;
    }

    public boolean isStock()
    {
        return this.stock;
    }

    public boolean isProfit()
    {
        return this.profit;
    }

    public String toString()
    {
        return new StringBuilder(Corporation.class.getSimpleName())
                   .append('(')
                   .append("stock=").append(String.format("\"%s\"", isStock())).append(", ")
                   .append("profit=").append(String.format("\"%s\"", isProfit())).append(", ")
                   .append("name=").append(String.format("\"%s\"", getName()))
                   .append(')')
                   .toString();
    }
}