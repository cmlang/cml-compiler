class _Employment:

    _singleton = None

    def __new__(cls) -> '_Employment':
        if cls._singleton is None:
            cls._singleton = super(_Employment, cls).__new__(cls)
        return cls._singleton

    def __init__(self) -> 'None':
        self.__employer = {} # type: Dict[Employee, Organization]
        self.__employees = {} # type: Dict[Organization, List[Employee]]

    def link(self, organization: 'Organization', employee: 'Employee') -> 'None':
        self.__employer[employee] = organization

        if self.__employees[organization] is None:
            employee_list = [employee]
        else:
            employee_list = self.__employees[organization]
        if not (employee in employee_list):
            employee_list.append(employee)
        self.__employees[organization] = employee_list

    def employer_of(self, employee: 'Employee') -> 'Organization':
        return self.__employer[employee]

    def employees_of(self, organization: 'Organization') -> 'List[Employee]':
        employee_list = self.__employees[organization]
        if employee_list is None:
            employee_list = []
        return list(employee_list)