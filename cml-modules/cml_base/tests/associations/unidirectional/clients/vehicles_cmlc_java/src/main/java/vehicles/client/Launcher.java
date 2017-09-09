package vehicles.client;

import vehicles.cmlc.Employee;
import vehicles.cmlc.Organization;
import vehicles.cmlc.Vehicle;

import static java.util.Collections.singletonList;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Unidirectional Associations (cmlc_java)\n");

        final Employee employee = Employee.createEmployee("John");
        final Organization organization = Organization.createOrganization("Acme", singletonList(employee));
        final Vehicle vehicle = Vehicle.createVehicle("ABC12345", employee, organization);

        System.out.println("Employee: " + employee);
        System.out.println("Organization: " + organization);
        System.out.println("Organization's Employees: " + organization.getEmployees());
        System.out.println("Vehicle: " + vehicle);
    }
}
