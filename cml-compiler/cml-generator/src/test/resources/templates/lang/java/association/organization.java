class Organization
{
    private final String name;
    private final List<Employee> employees;

    public Organization(String name, List<Employee> employees)
    {
        this.name = name;
        this.employees = employees;
    }

    public String getName()
    {
        return this.name;
    }

    public List<Employee> getEmployees()
    {
        return Collections.unmodifiableList(this.employees);
    }

    public String toString()
    {
        return new StringBuilder(Organization.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName())).append(", ")
                   .append("employees=").append(getEmployees())
                   .append(')')
                   .toString();
    }

    private static Employment employment;

    static void setEmployment(Employment association)
    {
        employment = association;
    }
}