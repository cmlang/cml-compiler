package vehicles.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

public class Employment
{
    private static final Employment singleton = new Employment();

    static void init(Class<?> cls)
    {
        if (Employee.class.isAssignableFrom(cls))
        {
            EmployeeImpl.setEmployment(singleton);
        }
        if (Organization.class.isAssignableFrom(cls))
        {
            OrganizationImpl.setEmployment(singleton);
        }
    }

    private final Map<Employee, Organization> employer = new HashMap<>();
    private final Map<Organization, Set<Employee>> employees = new HashMap<>();

    synchronized void linkMany(Organization employer, List<Employee> employees)
    {
        for (Employee employee: employees) link(employer, employee);
    }

    synchronized void link(Organization organization, Employee employee)
    {
        this.employer.put(employee, organization);

        final Set<Employee> employeeList = this.employees.computeIfAbsent(organization, key -> new LinkedHashSet<>());
        if (!employeeList.contains(employee))
        {
            employeeList.add(employee);
        }
    }

    synchronized Optional<Organization> employerOf(Employee employee)
    {
        return Optional.ofNullable(this.employer.get(employee));
    }

    synchronized List<Employee> employeesOf(Organization organization)
    {
        final Set<Employee> employeeList = this.employees.get(organization);

        return (employeeList == null) ? Collections.emptyList() : new ArrayList<>(employeeList);
    }
}