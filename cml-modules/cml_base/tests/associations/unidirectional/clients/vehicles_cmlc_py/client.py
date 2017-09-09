
from cml_associations_unidirectional_vehicles_cmlc import *

print("Unidirectional Associations (cmlc_py)\n")

employee = Employee.create_employee("John")
organization = Organization.create_organization("Acme", [employee])
vehicle = Vehicle.create_vehicle("ABC12345", employee, organization)

print("Employee: %s" % employee)
print("Organization: %s" % organization)
print("Organization's Employees: %s" % ','.join(map(str, organization.employees)))
print("Vehicle: %s" % vehicle)
