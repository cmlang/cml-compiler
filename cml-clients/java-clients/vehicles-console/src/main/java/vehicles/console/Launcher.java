package vehicles.console;

import vehicles.Employee;
import vehicles.Organization;
import vehicles.Vehicle;

import static java.util.Collections.emptyList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Vehicles Console");
        System.out.println("----------------");
        System.out.println();

        final Organization organization = new Organization("Walt Disney", emptyList(), emptyList());
        final Employee donald = new Employee("Donald Duck", organization);
        final Vehicle vehicle = new Vehicle("DUCK", donald, organization);

        System.out.println(organization);
        System.out.println(donald);
        System.out.println(vehicle);
    }
}
