class Employee:

    _employment = _Employment()

    def __init__(self, number: 'int', name: 'str', employer: 'Organization') -> 'None':
        self.__number = number
        self.__name = name

        self._employment.link(employer, self)

    @property
    def number(self) -> 'int':
        return self.__number

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def employer(self) -> 'Organization':
        return self._employment.employer_of(self)

    @property
    def employer_name(self) -> 'str':
        return self.employer.name

    @property
    def self_employee(self) -> 'Employee':
        return self

    @property
    def alias(self) -> 'str':
        return self.name

    def __str__(self) -> 'str':
        return "%s(number=%s, name=%s, employer_name=%s, alias=%s)" % (
            type(self).__name__,
            self.number,
            self.name,
            self.employer_name,
            self.alias
        )