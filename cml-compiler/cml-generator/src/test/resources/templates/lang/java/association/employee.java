class Employee
{
    private static Employment employment;

    private final String name;
    private final Organization employer;

    public Employee(String name, Organization employer)
    {
        this.name = name;
        this.employer = employer;
    }

    public String getName()
    {
        return this.name;
    }

    public Organization getEmployer()
    {
        return this.employer;
    }

    public String toString()
    {
        return new StringBuilder(Employee.class.getSimpleName())
                   .append('(')
                   .append("name=").append(String.format("\"%s\"", getName())).append(", ")
                   .append("employer=").append(String.format("\"%s\"", getEmployer()))
                   .append(')')
                   .toString();
    }

    static void setEmployment(Employment association)
    {
        employment = association;
    }
}