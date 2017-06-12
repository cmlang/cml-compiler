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
        final Employee mickey = new Employee("Mickey Mouse", organization);
        final Vehicle duck = new Vehicle("DUCK", donald, organization);
        final Vehicle mouse = new Vehicle("MOUSE", mickey, organization);

        System.out.println(organization);
        System.out.println("- Employees: " + organization.getEmployees());
        System.out.println("- Fleet: " + organization.getFleet());
        System.out.println();

        System.out.println(donald);
        System.out.println("- Employer: " + donald.getEmployer());
        System.out.println();

        System.out.println(mickey);
        System.out.println("- Employer: " + mickey.getEmployer());
        System.out.println();

        System.out.println(duck);
        System.out.println("- Owner: " + duck.getOwner());
        System.out.println();

        System.out.println(mouse);
        System.out.println("- Owner: " + mouse.getOwner());
        System.out.println();
    }
}
