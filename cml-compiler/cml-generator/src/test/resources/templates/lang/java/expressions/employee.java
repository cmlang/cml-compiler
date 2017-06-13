class Employee
{
    private static Employment employment;

    private final String name;

    public Employee(String name, Organization employer)
    {
        this.name = name;

        employment.link(employer, this);
    }

    public String getName()
    {
        return this.name;
    }

    public Organization getEmployer()
    {
        return employment.employerOf(this).get();
    }

    public String getEmployerName()
    {
        return getEmployer()
                   .getName();
    }

    public Employee getSelfEmployee()
    {
        return this;
    }

    public String toString()
    {
        return new StringBuilder(Employee.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName())).append(", ")
                   .append("employerName=").append(String.format("\"%s\"", getEmployerName()))
                   .append(')')
                   .toString();
    }

    static void setEmployment(Employment association)
    {
        employment = association;
    }

    static
    {
        Employment.init(Employee.class);
    }
}