class Employee:

    _employment = _Employment()

    def __init__(self, name: 'str', employer: 'Organization') -> 'None':
        self.__name = name

        self._employment.link(employer, self)

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def employer(self) -> 'Organization':
        return self._employment.employer_of(self)

    def __str__(self) -> 'str':
        return "%s(name=%s)" % (
            type(self).__name__,
            self.name
        )