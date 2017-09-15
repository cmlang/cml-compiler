package vehicles.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

public class VehicleAssignment
{
    private static final VehicleAssignment singleton = new VehicleAssignment();

    static void init(Class<?> cls)
    {
        if (Vehicle.class.isAssignableFrom(cls))
        {
            Vehicle.setVehicleAssignment(singleton);
        }
        if (Employee.class.isAssignableFrom(cls))
        {
            Employee.setVehicleAssignment(singleton);
        }
    }

    private final Map<Vehicle, @Nullable Employee> driver = new HashMap<>();
    private final Map<Employee, @Nullable Vehicle> vehicle = new HashMap<>();

    synchronized void link(Employee employee, Vehicle vehicle)
    {
        this.driver.put(vehicle, employee);

        this.vehicle.put(employee, vehicle);
    }

    synchronized void link(Vehicle vehicle, Employee employee)
    {
        this.driver.put(vehicle, employee);

        this.vehicle.put(employee, vehicle);
    }

    synchronized Optional<Employee> driverOf(Vehicle vehicle)
    {
        return Optional.ofNullable(this.driver.get(vehicle));
    }

    synchronized Optional<Vehicle> vehicleOf(Employee employee)
    {
        return Optional.ofNullable(this.vehicle.get(employee));
    }
}