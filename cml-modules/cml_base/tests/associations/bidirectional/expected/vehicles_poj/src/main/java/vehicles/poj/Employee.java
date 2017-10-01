package vehicles.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Employee
{
    private static Employment employment;
    private static VehicleAssignment vehicleAssignment;

    private final String name;

    public Employee(String name, Organization employer, @Nullable Vehicle vehicle)
    {
        this.name = name;

        employment.link(employer, this);
        vehicleAssignment.link(vehicle, this);
    }

    public String getName()
    {
        return this.name;
    }

    public Organization getEmployer()
    {
        return employment.employerOf(this).get();
    }

    public Optional<Vehicle> getVehicle()
    {
        return vehicleAssignment.vehicleOf(this);
    }

    public String toString()
    {
        return new StringBuilder(Employee.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", this.getName()))
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