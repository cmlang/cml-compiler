package vehicles.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Corporation extends Organization
{
    boolean getStock();

    boolean getProfit();

    static Corporation createCorporation(String name, List<Employee> employees, List<Vehicle> fleet)
    {
        return createCorporation(name, employees, fleet, true, true);
    }

    static Corporation createCorporation(String name, List<Employee> employees, List<Vehicle> fleet, boolean stock, boolean profit)
    {
        return new CorporationImpl(name, employees, fleet, stock, profit);
    }

    static Corporation extendCorporation(Organization organization, boolean stock, boolean profit)
    {
        return new CorporationImpl(organization, stock, profit);
    }
}

class CorporationImpl implements Corporation
{
    private final Organization organization;

    private final boolean stock;
    private final boolean profit;

    CorporationImpl(String name, List<Employee> employees, List<Vehicle> fleet, boolean stock, boolean profit)
    {
        this.organization = Organization.extendOrganization(this, name, employees, fleet);
        this.stock = stock;
        this.profit = profit;
    }

    CorporationImpl(Organization organization, boolean stock, boolean profit)
    {
        this.organization = organization;
        this.stock = stock;
        this.profit = profit;
    }

    public boolean getStock()
    {
        return this.stock;
    }

    public boolean getProfit()
    {
        return this.profit;
    }

    public String getName()
    {
        return this.organization.getName();
    }

    public List<Employee> getEmployees()
    {
        return this.organization.getEmployees();
    }

    public List<Vehicle> getFleet()
    {
        return this.organization.getFleet();
    }

    public String toString()
    {
        return new StringBuilder(Corporation.class.getSimpleName())
                   .append('(')
                   .append("stock=").append(String.format("\"%s\"", getStock())).append(", ")
                   .append("profit=").append(String.format("\"%s\"", getProfit())).append(", ")
                   .append("name=").append(String.format("\"%s\"", getName()))
                   .append(')')
                   .toString();
    }
}