package vehicles.client;

import vehicles.cmlc.Employee;
import vehicles.cmlc.Organization;
import vehicles.cmlc.Vehicle;

import static java.util.Collections.emptyList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Bidirectional Associations (cmlc_java)\n");

        final Organization organization = Organization.createOrganization("Acme", emptyList(), emptyList());
        final Employee employee = Employee.createEmployee("John", organization);
        final Vehicle vehicle = Vehicle.createVehicle("ABC12345", employee, organization);

        System.out.println("Organization: " + organization);
        System.out.println("Organization's Employees: " + organization.getEmployees());
        System.out.println("Employee: " + employee);
        System.out.println("Employee's Employer: " + employee.getEmployer());
        System.out.println("Vehicle: " + vehicle);
        System.out.println("Vehicle's Driver: " + vehicle.getDriver());
        System.out.println("Vehicle's Owner: " + vehicle.getOwner());
    }
}
