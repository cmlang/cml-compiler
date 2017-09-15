package vehicles.client;

import vehicles.poj.Corporation;
import vehicles.poj.Employee;
import vehicles.poj.Vehicle;

import static java.util.Collections.emptyList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Bidirectional Associations (poj)\n");

        final Corporation corporation = new Corporation("Walt Disney", emptyList(), emptyList());
        final Vehicle mouse = new Vehicle("MOUSE", null, corporation);
        final Employee donald = new Employee("Donald Duck", corporation, null);
        final Employee mickey = new Employee("Mickey Mouse", corporation, mouse);
        final Vehicle duck = new Vehicle("DUCK", donald, corporation);

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
