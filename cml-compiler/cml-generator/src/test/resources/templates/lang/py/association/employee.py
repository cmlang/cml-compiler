class Employee:

    _employment = _Employment()

    def __init__(self, name: 'str', employer: 'Organization') -> 'None':
        self.__name = name
        self.__employer = employer

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def employer(self) -> 'Organization':
        return self.__employer

    def __str__(self) -> 'str':
        return "%s(name=%s, employer=%s)" % (
            type(self).__name__,
            self.name,
            self.employer
        )