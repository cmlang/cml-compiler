package vehicles.client;

import vehicles.cmlc.Corporation;
import vehicles.cmlc.Employee;
import vehicles.cmlc.Vehicle;

import static java.util.Collections.emptyList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Bidirectional Associations (cmlc_java)\n");

        final Corporation corporation = Corporation.createCorporation("Walt Disney", emptyList(), emptyList());
        final Vehicle duck = Vehicle.createVehicle("DUCK", null, corporation);
        final Employee donald = Employee.createEmployee("Donald Duck", corporation, duck);
        final Employee mickey = Employee.createEmployee("Mickey Mouse", corporation, null);
        final Vehicle mouse = Vehicle.createVehicle("MOUSE", mickey, corporation);

        System.out.println(corporation);
        System.out.println("- Employees: " + corporation.getEmployees());
        System.out.println("- Fleet: " + corporation.getFleet());
        System.out.println();

        System.out.println(donald);
        System.out.println("- Employer: " + donald.getEmployer());
        System.out.println("- Vehicle: " + donald.getVehicle());
        System.out.println();

        System.out.println(mickey);
        System.out.println("- Employer: " + mickey.getEmployer());
        System.out.println("- Vehicle: " + mickey.getVehicle());
        System.out.println();

        System.out.println(duck);
        System.out.println("- Owner: " + duck.getOwner());
        System.out.println("- Driver: " + duck.getDriver());
        System.out.println();

        System.out.println(mouse);
        System.out.println("- Owner: " + mouse.getOwner());
        System.out.println("- Driver: " + mouse.getDriver());
    }
}
