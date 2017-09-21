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
        self.__employer = {}  # type: Dict[Employee, Organization]
        self.__employees = {}  # type: Dict[Organization, List[Employee]]

    def link_many(self, employer: 'Organization', employees: 'List[Employee]') -> 'None':
        for employee in employees: self.link(organization=employer, employee=employee)

    def link(self, organization: 'Organization', employee: 'Employee') -> 'None':
        self.__employer[employee] = organization

        if organization in self.__employees:
            employee_list = self.__employees[organization]
        else:
            employee_list = [employee]
        if not (employee in employee_list):
            employee_list.append(employee)
        self.__employees[organization] = employee_list

    def employer_of(self, employee: 'Employee') -> 'Optional[Organization]':
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
        self.__owner = {}  # type: Dict[Vehicle, Organization]
        self.__fleet = {}  # type: Dict[Organization, List[Vehicle]]

    def link_many(self, owner: 'Organization', fleet: 'List[Vehicle]') -> 'None':
        for vehicle in fleet: self.link(organization=owner, vehicle=vehicle)

    def link(self, organization: 'Organization', vehicle: 'Vehicle') -> 'None':
        self.__owner[vehicle] = organization

        if organization in self.__fleet:
            vehicle_list = self.__fleet[organization]
        else:
            vehicle_list = [vehicle]
        if not (vehicle in vehicle_list):
            vehicle_list.append(vehicle)
        self.__fleet[organization] = vehicle_list

    def owner_of(self, vehicle: 'Vehicle') -> 'Optional[Organization]':
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


class _VehicleAssignment:

    _singleton = None

    def __new__(cls) -> '_VehicleAssignment':
        if cls._singleton is None:
            cls._singleton = super(_VehicleAssignment, cls).__new__(cls)
        return cls._singleton

    def __init__(self) -> 'None':
        self.__driver = {}  # type: Dict[Vehicle, Optional[Employee]]
        self.__vehicle = {}  # type: Dict[Employee, Optional[Vehicle]]

    def link(self, employee: 'Employee', vehicle: 'Vehicle') -> 'None':
        self.__driver[vehicle] = employee

        self.__vehicle[employee] = vehicle

    def driver_of(self, vehicle: 'Vehicle') -> 'Optional[Employee]':
        if vehicle in self.__driver:
            return self.__driver[vehicle]
        else:
            return None

    def vehicle_of(self, employee: 'Employee') -> 'Optional[Vehicle]':
        if employee in self.__vehicle:
            return self.__vehicle[employee]
        else:
            return None


class Vehicle(ABC):

    @abstractproperty
    def plate(self) -> 'str':
        pass

    @abstractproperty
    def driver(self) -> 'Optional[Employee]':
        pass

    @abstractproperty
    def owner(self) -> 'Organization':
        pass

    @staticmethod
    def create_vehicle(plate: 'str', driver: 'Optional[Employee]', owner: 'Organization') -> 'Vehicle':
        return VehicleImpl(None, plate, driver, owner)

    @staticmethod
    def extend_vehicle(actual_self: 'Optional[Vehicle]', plate: 'str', driver: 'Optional[Employee]', owner: 'Organization') -> 'Vehicle':
        return VehicleImpl(actual_self, plate, driver, owner)


class VehicleImpl(Vehicle):

    _vehicle_ownership = _VehicleOwnership()
    _vehicle_assignment = _VehicleAssignment()

    def __init__(self, actual_self: 'Optional[Vehicle]', plate: 'str', driver: 'Optional[Employee]', owner: 'Organization') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Vehicle]
        else:
            self.__actual_self = actual_self

        self.__plate = plate

        self._vehicle_assignment.link(employee=driver, vehicle=self.__actual_self)
        self._vehicle_ownership.link(organization=owner, vehicle=self.__actual_self)

    @property
    def plate(self) -> 'str':
        return self.__plate

    @property
    def driver(self) -> 'Optional[Employee]':
        return self._vehicle_assignment.driver_of(self.__actual_self)

    @property
    def owner(self) -> 'Organization':
        return self._vehicle_ownership.owner_of(self.__actual_self)

    def __str__(self) -> 'str':
        return "%s(plate=%s)" % (
            type(self).__name__,
            self.plate
        )


class Employee(ABC):

    @abstractproperty
    def name(self) -> 'str':
        pass

    @abstractproperty
    def employer(self) -> 'Organization':
        pass

    @abstractproperty
    def vehicle(self) -> 'Optional[Vehicle]':
        pass

    @staticmethod
    def create_employee(name: 'str', employer: 'Organization', vehicle: 'Optional[Vehicle]') -> 'Employee':
        return EmployeeImpl(None, name, employer, vehicle)

    @staticmethod
    def extend_employee(actual_self: 'Optional[Employee]', name: 'str', employer: 'Organization', vehicle: 'Optional[Vehicle]') -> 'Employee':
        return EmployeeImpl(actual_self, name, employer, vehicle)


class EmployeeImpl(Employee):

    _employment = _Employment()
    _vehicle_assignment = _VehicleAssignment()

    def __init__(self, actual_self: 'Optional[Employee]', name: 'str', employer: 'Organization', vehicle: 'Optional[Vehicle]') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Employee]
        else:
            self.__actual_self = actual_self

        self.__name = name

        self._employment.link(organization=employer, employee=self.__actual_self)
        self._vehicle_assignment.link(vehicle=vehicle, employee=self.__actual_self)

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def employer(self) -> 'Organization':
        return self._employment.employer_of(self.__actual_self)

    @property
    def vehicle(self) -> 'Optional[Vehicle]':
        return self._vehicle_assignment.vehicle_of(self.__actual_self)

    def __str__(self) -> 'str':
        return "%s(name=%s)" % (
            type(self).__name__,
            self.name
        )


class Organization(ABC):

    @abstractproperty
    def name(self) -> 'str':
        pass

    @abstractproperty
    def employees(self) -> 'List[Employee]':
        pass

    @abstractproperty
    def fleet(self) -> 'List[Vehicle]':
        pass

    @staticmethod
    def extend_organization(actual_self: 'Optional[Organization]', name: 'str', employees: 'List[Employee]', fleet: 'List[Vehicle]') -> 'Organization':
        return OrganizationImpl(actual_self, name, employees, fleet)


class OrganizationImpl(Organization):

    _employment = _Employment()
    _vehicle_ownership = _VehicleOwnership()

    def __init__(self, actual_self: 'Optional[Organization]', name: 'str', employees: 'List[Employee]', fleet: 'List[Vehicle]') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Organization]
        else:
            self.__actual_self = actual_self

        self.__name = name

        self._employment.link_many(self.__actual_self, employees)
        self._vehicle_ownership.link_many(self.__actual_self, fleet)

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def employees(self) -> 'List[Employee]':
        return self._employment.employees_of(self.__actual_self)

    @property
    def fleet(self) -> 'List[Vehicle]':
        return self._vehicle_ownership.fleet_of(self.__actual_self)

    def __str__(self) -> 'str':
        return "%s(name=%s)" % (
            type(self).__name__,
            self.name
        )


class Corporation(Organization, ABC):

    @abstractproperty
    def stock(self) -> 'bool':
        pass

    @abstractproperty
    def profit(self) -> 'bool':
        pass

    @staticmethod
    def create_corporation(name: 'str', employees: 'List[Employee]', fleet: 'List[Vehicle]', stock: 'bool' = True, profit: 'bool' = True) -> 'Corporation':
        return CorporationImpl(None, stock, profit, name=name, employees=employees, fleet=fleet)

    @staticmethod
    def extend_corporation(organization: 'Organization', stock: 'bool' = True, profit: 'bool' = True) -> 'Corporation':
        return CorporationImpl(organization, stock, profit)


class CorporationImpl(Corporation):

    def __init__(self, organization: 'Optional[Organization]', stock: 'bool' = True, profit: 'bool' = True, **kwargs) -> 'None':
        if organization is None:
            self.__organization = Organization.extend_organization(self, kwargs['name'], kwargs['employees'], kwargs['fleet'])
        else:
            self.__organization = organization
        self.__stock = stock
        self.__profit = profit

    @property
    def stock(self) -> 'bool':
        return self.__stock

    @property
    def profit(self) -> 'bool':
        return self.__profit

    @property
    def name(self) -> 'str':
        return self.__organization.name

    @property
    def employees(self) -> 'List[Employee]':
        return self.__organization.employees

    @property
    def fleet(self) -> 'List[Vehicle]':
        return self.__organization.fleet

    def __str__(self) -> 'str':
        return "%s(stock=%s, profit=%s, name=%s)" % (
            type(self).__name__,
            self.stock,
            self.profit,
            self.name
        )