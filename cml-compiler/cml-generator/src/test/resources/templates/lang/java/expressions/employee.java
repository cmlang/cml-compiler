class Employee
{
    private static Employment employment;

    private final int number;
    private final String name;

    public Employee(int number, String name, Organization employer)
    {
        this.number = number;
        this.name = name;

        employment.link(employer, this);
    }

    public int getNumber()
    {
        return this.number;
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

    public String getAlias()
    {
        return getName();
    }

    public String toString()
    {
        return new StringBuilder(Employee.class.getSimpleName())
                   .append('(')
                   .append("number=").append(String.format("\"%s\"", getNumber())).append(", ")
                   .append("name=").append(String.format("\"%s\"", getName())).append(", ")
                   .append("employerName=").append(String.format("\"%s\"", getEmployerName())).append(", ")
                   .append("alias=").append(String.format("\"%s\"", getAlias()))
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