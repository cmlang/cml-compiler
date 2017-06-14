class Organization
{
    private static Employment employment;

    private final String name;

    public Organization(String name, List<Employee> employees)
    {
        this.name = name;

        employment.linkMany(this, employees);
    }

    public String getName()
    {
        return this.name;
    }

    public List<Employee> getEmployees()
    {
        return employment.employeesOf(this);
    }

    public List<String> getEmployeeNames()
    {
        return getEmployees()
                   .stream()
                   .map(employee -> employee.getName())
                   .collect(toList());
    }

    public List<String> getEmployerNames()
    {
        return getEmployees()
                   .stream()
                   .map(employee -> employee.getEmployer())
                   .map(organization -> organization.getName())
                   .collect(toList());
    }

    public List<Organization> getEmployers()
    {
        return getEmployees()
                   .stream()
                   .map(employee -> employee.getEmployer())
                   .collect(toList());
    }

    public List<Integer> getEmployeeNumbers()
    {
        return getEmployees()
                   .stream()
                   .map(employee -> employee.getNumber())
                   .collect(toList());
    }

    public String toString()
    {
        return new StringBuilder(Organization.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName())).append(", ")
                   .append("employeeNames=").append(getEmployeeNames()).append(", ")
                   .append("employerNames=").append(getEmployerNames()).append(", ")
                   .append("employeeNumbers=").append(getEmployeeNumbers())
                   .append(')')
                   .toString();
    }

    static void setEmployment(Employment association)
    {
        employment = association;
    }

    static
    {
        Employment.init(Organization.class);
    }
}