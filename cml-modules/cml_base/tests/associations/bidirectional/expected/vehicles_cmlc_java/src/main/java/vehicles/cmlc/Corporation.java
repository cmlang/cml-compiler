package vehicles.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface Corporation extends Organization
{
    boolean isStock();

    boolean isProfit();

    Corporation getMyself();

    static Corporation createCorporation(String name, List<Employee> employees, List<Vehicle> fleet)
    {
        return createCorporation(name, employees, fleet, true, true);
    }

    static Corporation createCorporation(String name, List<Employee> employees, List<Vehicle> fleet, boolean stock, boolean profit)
    {
        return new CorporationImpl(null, name, employees, fleet, stock, profit);
    }

    static Corporation extendCorporation(@Nullable Corporation actual_self, Organization organization, boolean stock, boolean profit)
    {
        return new CorporationImpl(actual_self, organization, stock, profit);
    }
}

class CorporationImpl implements Corporation
{
    private final @Nullable Corporation actual_self;

    private final Organization organization;

    private final boolean stock;
    private final boolean profit;

    CorporationImpl(@Nullable Corporation actual_self, String name, List<Employee> employees, List<Vehicle> fleet, boolean stock, boolean profit)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.organization = Organization.extendOrganization(this.actual_self, name, employees, fleet);
        this.stock = stock;
        this.profit = profit;
    }

    CorporationImpl(@Nullable Corporation actual_self, Organization organization, boolean stock, boolean profit)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.organization = organization;
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

    public Corporation getMyself()
    {
        return this.actual_self;
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
                   .append("stock=").append(String.format("\"%s\"", this.actual_self.isStock())).append(", ")
                   .append("profit=").append(String.format("\"%s\"", this.actual_self.isProfit())).append(", ")
                   .append("name=").append(String.format("\"%s\"", this.actual_self.getName()))
                   .append(')')
                   .toString();
    }
}