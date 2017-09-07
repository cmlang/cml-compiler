package vehicles.client;

import vehicles.poj.Employee;
import vehicles.poj.Organization;
import vehicles.poj.Vehicle;

import static java.util.Collections.singletonList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Unidirectional Associations\n");

        final Employee employee = new Employee("John");
        final Organization organization = new Organization("Acme", singletonList(employee));
        final Vehicle vehicle = new Vehicle("ABC12345", employee, organization);

        System.out.println("Employee: " + employee);
        System.out.println("Organization: " + organization);
        System.out.println("Organization's Employees: " + organization.getEmployees());
        System.out.println("Vehicle: " + vehicle);
    }
}
