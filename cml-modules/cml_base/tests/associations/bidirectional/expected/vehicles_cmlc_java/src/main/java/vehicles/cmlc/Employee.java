package vehicles.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Employee
{
    String getName();

    Organization getEmployer();

    Optional<Vehicle> getVehicle();

    static Employee createEmployee(String name, Organization employer, @Nullable Vehicle vehicle)
    {
        return new EmployeeImpl(null, name, employer, vehicle);
    }

    static Employee extendEmployee(@Nullable Employee actual_self, String name, Organization employer, @Nullable Vehicle vehicle)
    {
        return new EmployeeImpl(actual_self, name, employer, vehicle);
    }
}

class EmployeeImpl implements Employee
{
    private static Employment employment;
    private static VehicleAssignment vehicleAssignment;

    private final @Nullable Employee actual_self;

    private final String name;

    EmployeeImpl(@Nullable Employee actual_self, String name, Organization employer, @Nullable Vehicle vehicle)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.name = name;

        employment.link(employer, this.actual_self);
        vehicleAssignment.link(vehicle, this.actual_self);
    }

    public String getName()
    {
        return this.name;
    }

    public Organization getEmployer()
    {
        return employment.employerOf(actual_self).get();
    }

    public Optional<Vehicle> getVehicle()
    {
        return vehicleAssignment.vehicleOf(actual_self);
    }

    public String toString()
    {
        return new StringBuilder(Employee.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName()))
                   .append(')')
                   .toString();
    }

    static void setEmployment(Employment association)
    {
        employment = association;
    }

    static void setVehicleAssignment(VehicleAssignment association)
    {
        vehicleAssignment = association;
    }

    static
    {
        Employment.init(Employee.class);
        VehicleAssignment.init(Employee.class);
    }
}