package vehicles.poj;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Vehicle
{
    private static VehicleOwnership vehicleOwnership;
    private static VehicleAssignment vehicleAssignment;

    private final String plate;

    public Vehicle(String plate, @Nullable Employee driver, Organization owner)
    {
        this.plate = plate;

        vehicleAssignment.link(driver, this);
        vehicleOwnership.link(owner, this);
    }

    public String getPlate()
    {
        return this.plate;
    }

    public Optional<Employee> getDriver()
    {
        return vehicleAssignment.driverOf(this);
    }

    public Organization getOwner()
    {
        return vehicleOwnership.ownerOf(this).get();
    }

    public String toString()
    {
        return new StringBuilder(Vehicle.class.getSimpleName())
                   .append('(')
                   .append("plate=").append(String.format("\"%s\"", this.getPlate()))
                   .append(')')
                   .toString();
    }

    static void setVehicleOwnership(VehicleOwnership association)
    {
        vehicleOwnership = association;
    }

    static void setVehicleAssignment(VehicleAssignment association)
    {
        vehicleAssignment = association;
    }

    static
    {
        VehicleOwnership.init(Vehicle.class);
        VehicleAssignment.init(Vehicle.class);
    }
}