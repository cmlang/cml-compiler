package vehicles.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Organization
{
    String getName();

    List<Employee> getEmployees();

    List<Vehicle> getFleet();

    static Organization createOrganization(String name, List<Employee> employees, List<Vehicle> fleet)
    {
        return new OrganizationImpl(name, employees, fleet);
    }

    static Organization extendOrganization(String name, List<Employee> employees, List<Vehicle> fleet)
    {
        return new OrganizationImpl(name, employees, fleet);
    }
}

class OrganizationImpl implements Organization
{
    private static Employment employment;
    private static VehicleOwnership vehicleOwnership;

    private final String name;

    public OrganizationImpl(String name, List<Employee> employees, List<Vehicle> fleet)
    {
        this.name = name;

        employment.linkMany(this, employees);
        vehicleOwnership.linkMany(this, fleet);
    }

    public String getName()
    {
        return this.name;
    }

    public List<Employee> getEmployees()
    {
        return employment.employeesOf(this);
    }

    public List<Vehicle> getFleet()
    {
        return vehicleOwnership.fleetOf(this);
    }

    public String toString()
    {
        return new StringBuilder(Organization.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName()))
                   .append(')')
                   .toString();
    }

    static void setEmployment(Employment association)
    {
        employment = association;
    }

    static void setVehicleOwnership(VehicleOwnership association)
    {
        vehicleOwnership = association;
    }

    static
    {
        Employment.init(Organization.class);
        VehicleOwnership.init(Organization.class);
    }
}