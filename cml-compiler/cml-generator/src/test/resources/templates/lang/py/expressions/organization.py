class Organization:

    _employment = _Employment()

    def __init__(self, name: 'str', employees: 'List[Employee]') -> 'None':
        self.__name = name

        self._employment.link_many(self, employees)

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def employees(self) -> 'List[Employee]':
        return self._employment.employees_of(self)

    @property
    def employee_names(self) -> 'List[str]':
        return list(
            map(
                lambda employee: employee.name,
                self.employees
            )
        )

    @property
    def employer_names(self) -> 'List[str]':
        return list(
            map(
                lambda organization: organization.name,
                map(
                    lambda employee: employee.employer,
                    self.employees
                )
            )
        )

    @property
    def employers(self) -> 'List[Organization]':
        return list(
            map(
                lambda employee: employee.employer,
                self.employees
            )
        )

    @property
    def employee_numbers(self) -> 'List[int]':
        return list(
            map(
                lambda employee: employee.number,
                self.employees
            )
        )

    def __str__(self) -> 'str':
        return "%s(name=%s, employee_names=%s, employer_names=%s, employee_numbers=%s)" % (
            type(self).__name__,
            self.name,
            self.employee_names,
            self.employer_names,
            self.employee_numbers
        )