class Organization:

    def __init__(self, name: 'str', employees: 'List[Employee]') -> 'None':
        self.__name = name
        self.__employees = employees

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def employees(self) -> 'List[Employee]':
        return self.__employees

    def __str__(self) -> 'str':
        return "%s(name=%s, employees=%s)" % (
            type(self).__name__,
            self.name,
            self.employees
        )

    @staticmethod
    def set_employment(association: '_Employment') -> 'None':
        _employment = association