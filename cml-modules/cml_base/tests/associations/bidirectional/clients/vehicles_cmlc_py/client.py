
from cml_associations_bidirectional_vehicles_cmlc import *

print("Bidirectional Associations (cmlc_py)\n")

organization = Organization.create_organization("Walt Disney", [], [])
donald = Employee.create_employee("Donald Duck", organization)
mickey = Employee.create_employee("Mickey Mouse", organization)
duck = Vehicle.create_vehicle("DUCK", donald, organization)
mouse = Vehicle.create_vehicle("MOUSE", mickey, organization)

print(organization)
print("- Employees: %s" % ', '.join(map(str, organization.employees)))
print("- Fleet: %s" % ', '.join(map(str, organization.fleet)))
print()

print(donald)
print("- Employer: %s" % donald.employer)
print()

print(mickey)
print("- Employer: %s" % mickey.employer)
print()

print(duck)
print("- Owner: %s" % duck.owner)
print("- Driver: %s" % duck.driver)
print()

print(mouse)
print("- Owner: %s" % mouse.owner)
print("- Driver: %s" % mouse.driver)

