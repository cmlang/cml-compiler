class _Employment:

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