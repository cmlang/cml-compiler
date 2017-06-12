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

    def __str__(self) -> 'str':
        return "%s(name=%s)" % (
            type(self).__name__,
            self.name
        )