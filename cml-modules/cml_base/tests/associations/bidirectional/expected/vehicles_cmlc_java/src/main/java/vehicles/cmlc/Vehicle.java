package vehicles.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Vehicle
{
    String getPlate();

    Optional<Employee> getDriver();

    Organization getOwner();

    static Vehicle createVehicle(String plate, @Nullable Employee driver, Organization owner)
    {
        return new VehicleImpl(null, plate, driver, owner);
    }

    static Vehicle extendVehicle(@Nullable Vehicle actual_self, String plate, @Nullable Employee driver, Organization owner)
    {
        return new VehicleImpl(actual_self, plate, driver, owner);
    }
}

class VehicleImpl implements Vehicle
{
    private static VehicleOwnership vehicleOwnership;

    private final @Nullable Vehicle actual_self;

    private final String plate;
    private final @Nullable Employee driver;

    VehicleImpl(@Nullable Vehicle actual_self, String plate, @Nullable Employee driver, Organization owner)
    {
        this.actual_self = actual_self == null ? this : actual_self;
        this.plate = plate;
        this.driver = driver;

        vehicleOwnership.link(owner, this.actual_self);
    }

    public String getPlate()
    {
        return this.plate;
    }

    public Optional<Employee> getDriver()
    {
        return Optional.ofNullable(this.driver);
    }

    public Organization getOwner()
    {
        return vehicleOwnership.ownerOf(actual_self).get();
    }

    public String toString()
    {
        return new StringBuilder(Vehicle.class.getSimpleName())
                   .append('(')
                   .append("plate=").append(String.format("\"%s\"", getPlate())).append(", ")
                   .append("driver=").append(getDriver().isPresent() ? String.format("\"%s\"", getDriver()) : "not present")
                   .append(')')
                   .toString();
    }

    static void setVehicleOwnership(VehicleOwnership association)
    {
        vehicleOwnership = association;
    }

    static
    {
        VehicleOwnership.init(Vehicle.class);
    }
}