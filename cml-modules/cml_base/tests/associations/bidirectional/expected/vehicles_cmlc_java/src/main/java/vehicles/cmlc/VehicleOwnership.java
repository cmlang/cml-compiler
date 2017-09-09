package vehicles.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

public class VehicleOwnership
{
    private static final VehicleOwnership singleton = new VehicleOwnership();

    static void init(Class<?> cls)
    {
        if (Vehicle.class.isAssignableFrom(cls))
        {
            VehicleImpl.setVehicleOwnership(singleton);
        }
        if (Organization.class.isAssignableFrom(cls))
        {
            OrganizationImpl.setVehicleOwnership(singleton);
        }
    }

    private final Map<Vehicle, Organization> owner = new HashMap<>();
    private final Map<Organization, List<Vehicle>> fleet = new HashMap<>();

    synchronized void linkMany(Organization owner, List<Vehicle> fleet)
    {
        for (Vehicle vehicle: fleet) link(owner, vehicle);
    }

    synchronized void link(Organization organization, Vehicle vehicle)
    {
        this.owner.put(vehicle, organization);

        final List<Vehicle> vehicleList = this.fleet.computeIfAbsent(organization, key -> new ArrayList<>());
        if (!vehicleList.contains(vehicle))
        {
            vehicleList.add(vehicle);
        }
    }

    synchronized Optional<Organization> ownerOf(Vehicle vehicle)
    {
        return Optional.ofNullable(this.owner.get(vehicle));
    }

    synchronized List<Vehicle> fleetOf(Organization organization)
    {
        final List<Vehicle> vehicleList = this.fleet.get(organization);

        return (vehicleList == null) ? Collections.emptyList() : new ArrayList<>(vehicleList);
    }
}