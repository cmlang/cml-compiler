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

    static Organization extendOrganization(@Nullable Organization actual_self, String name, List<Employee> employees, List<Vehicle> fleet)
    {
        return new OrganizationImpl(actual_self, name, employees, fleet);
    }
}

class OrganizationImpl implements Organization
{
    private static Employment employment;
    private static VehicleOwnership vehicleOwnership;

    private final @Nullable Organization actual_self;

    private final String name;

    OrganizationImpl(@Nullable Organization actual_self, String name, List<Employee> employees, List<Vehicle> fleet)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.name = name;

        employment.linkMany(this.actual_self, employees);
        vehicleOwnership.linkMany(this.actual_self, fleet);
    }

    public String getName()
    {
        return this.name;
    }

    public List<Employee> getEmployees()
    {
        return employment.employeesOf(actual_self);
    }

    public List<Vehicle> getFleet()
    {
        return vehicleOwnership.fleetOf(actual_self);
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