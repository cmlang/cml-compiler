
from cml_associations_unidirectional_vehicles_pop import *

print("Unidirectional Associations\n")

employee = Employee("John");
organization = Organization("Acme", [employee]);
vehicle = Vehicle("ABC12345", employee, organization);

print("Employee: %s" % employee)
print("Organization: %s" % organization)
print("Organization's Employees: %s" % ','.join(map(str, organization.employees)))
print("Vehicle: %s" % vehicle)
