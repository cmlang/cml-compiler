package vehicles.client;

import vehicles.poj.Employee;
import vehicles.poj.Organization;
import vehicles.poj.Vehicle;

import static java.util.Collections.emptyList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Bidirectional Associations (poj)\n");

        final Organization organization = new Organization("Acme", emptyList(), emptyList());
        final Employee employee = new Employee("John", organization);
        final Vehicle vehicle = new Vehicle("ABC12345", employee, organization);

        System.out.println("Organization: " + organization);
        System.out.println("Organization's Employees: " + organization.getEmployees());
        System.out.println("Employee: " + employee);
        System.out.println("Employee's Employer: " + employee.getEmployer());
        System.out.println("Vehicle: " + vehicle);
        System.out.println("Vehicle's Driver: " + vehicle.getDriver());
        System.out.println("Vehicle's Owner: " + vehicle.getOwner());
    }
}
