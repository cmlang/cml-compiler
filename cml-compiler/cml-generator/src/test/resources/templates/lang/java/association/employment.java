public class Employment
{
    private final Map<Employee, Organization> employer = new HashMap<>();
    private final Map<Organization, List<Employee>> employees = new HashMap<>();
}