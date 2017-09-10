
from cml_associations_bidirectional_vehicles_cmlc import *

print("Bidirectional Associations (cmlc_py)\n")

corporation = Corporation.create_corporation("Walt Disney", [], [])
donald = Employee.create_employee("Donald Duck", corporation)
mickey = Employee.create_employee("Mickey Mouse", corporation)
duck = Vehicle.create_vehicle("DUCK", donald, corporation)
mouse = Vehicle.create_vehicle("MOUSE", mickey, corporation)

print(corporation)
print("- Employees: %s" % ', '.join(map(str, corporation.employees)))
print("- Fleet: %s" % ', '.join(map(str, corporation.fleet)))
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

