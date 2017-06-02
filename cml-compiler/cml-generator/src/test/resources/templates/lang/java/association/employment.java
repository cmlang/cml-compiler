public class Employment
{
    private final Map<Employee, Organization> employer = new HashMap<>();
    private final Map<Organization, List<Employee>> employees = new HashMap<>();

    synchronized void link(Organization organization, Employee employee)
    {
        this.employer.put(employee, organization);

        final List<Employee> employeeList = this.employees.computeIfAbsent(organization, key -> new ArrayList<>());
        if (!employeeList.contains(employee))
        {
            employeeList.add(employee);
        }
    }
}