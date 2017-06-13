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

    public String getEmployeeNames()
    {
        return getEmployees().getName();
    }

    public String toString()
    {
        return new StringBuilder(Organization.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName())).append(", ")
                   .append("employeeNames=").append(String.format("\"%s\"", getEmployeeNames()))
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