from typing import *
from abc import *
from decimal import *

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


class _VehicleOwnership:

    _singleton = None

    def __new__(cls) -> '_VehicleOwnership':
        if cls._singleton is None:
            cls._singleton = super(_VehicleOwnership, cls).__new__(cls)
        return cls._singleton

    def __init__(self) -> 'None':
        self.__owner = {} # type: Dict[Vehicle, Organization]
        self.__fleet = {} # type: Dict[Organization, List[Vehicle]]

    def link_many(self, owner: 'Organization', fleet: 'List[Vehicle]') -> 'None':
        for vehicle in fleet: self.link(owner, vehicle)

    def link(self, organization: 'Organization', vehicle: 'Vehicle') -> 'None':
        self.__owner[vehicle] = organization

        if organization in self.__fleet:
            vehicle_list = self.__fleet[organization]
        else:
            vehicle_list = [vehicle]
        if not (vehicle in vehicle_list):
            vehicle_list.append(vehicle)
        self.__fleet[organization] = vehicle_list

    def owner_of(self, vehicle: 'Vehicle') -> 'Organization':
        if vehicle in self.__owner:
            return self.__owner[vehicle]
        else:
            return None

    def fleet_of(self, organization: 'Organization') -> 'List[Vehicle]':
        if organization in self.__fleet:
            vehicle_list = self.__fleet[organization]
        else:
            vehicle_list = []
        return list(vehicle_list)


class Vehicle:

    _vehicle_ownership = _VehicleOwnership()

    def __init__(self, plate: 'str', driver: 'Employee', owner: 'Organization') -> 'None':
        self.__plate = plate
        self.__driver = driver

        self._vehicle_ownership.link(owner, self)

    @property
    def plate(self) -> 'str':
        return self.__plate

    @property
    def driver(self) -> 'Employee':
        return self.__driver

    @property
    def owner(self) -> 'Organization':
        return self._vehicle_ownership.owner_of(self)

    def __str__(self) -> 'str':
        return "%s(plate=%s, driver=%s)" % (
            type(self).__name__,
            self.plate,
            self.driver
        )


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


class Organization:

    _employment = _Employment()
    _vehicle_ownership = _VehicleOwnership()

    def __init__(self, name: 'str', employees: 'List[Employee]', fleet: 'List[Vehicle]') -> 'None':
        self.__name = name

        self._employment.link_many(self, employees)
        self._vehicle_ownership.link_many(self, fleet)

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def employees(self) -> 'List[Employee]':
        return self._employment.employees_of(self)

    @property
    def fleet(self) -> 'List[Vehicle]':
        return self._vehicle_ownership.fleet_of(self)

    def __str__(self) -> 'str':
        return "%s(name=%s)" % (
            type(self).__name__,
            self.name
        )