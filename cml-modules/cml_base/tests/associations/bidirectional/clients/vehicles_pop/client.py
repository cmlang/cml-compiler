
from cml_associations_bidirectional_vehicles_pop import *

print("Bidirectional Associations (pop)\n")

organization = Organization("Acme", [], [])
employee = Employee("John", organization)
vehicle = Vehicle("ABC12345", employee, organization)

print("Organization: %s" % organization)
print("Organization's Employees: %s" % ','.join(map(str, organization.employees)))
print("Employee: %s" % employee)
print("Employee's Employer: %s" % employee.employer)
print("Vehicle: %s" % vehicle)
print("Vehicle's Driver: %s" % vehicle.driver)
print("Vehicle's Owner: %s" % vehicle.owner)
