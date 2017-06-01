class _Employment:

    def __init__(self) -> 'None':
        self.__employer = {} # type: Dict[Employee, Organization]
        self.__employees = {} # type: Dict[Organization, List[Employee]]