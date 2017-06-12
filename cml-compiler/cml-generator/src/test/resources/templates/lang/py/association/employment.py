class _Employment:

    _singleton = None

    def __new__(cls) -> '_Employment':
        if cls._singleton is None:
            cls._singleton = super(_Employment, cls).__new__(cls)
        return cls._singleton

    def __init__(self) -> 'None':
        self.__employer = {} # type: Dict[Employee, Organization]
        self.__employees = {} # type: Dict[Organization, List[Employee]]

    def link_many(self, employer: 'Organization', employees: 'List[Employee]') -> 'None':
        for employee in employees: self.link(employer, employee)

    def link(self, organization: 'Organization', employee: 'Employee') -> 'None':
        self.__employer[employee] = organization

        if organization in self.__employees:
            employee_list = self.__employees[organization]
        else:
            employee_list = [employee]
        if not (employee in employee_list):
            employee_list.append(employee)
        self.__employees[organization] = employee_list

    def employer_of(self, employee: 'Employee') -> 'Organization':
        if employee in self.__employer:
            return self.__employer[employee]
        else:
            return None

    def employees_of(self, organization: 'Organization') -> 'List[Employee]':
        if organization in self.__employees:
            employee_list = self.__employees[organization]
        else:
            employee_list = []
        return list(employee_list)