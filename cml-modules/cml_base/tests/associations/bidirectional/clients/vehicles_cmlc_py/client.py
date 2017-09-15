
from cml_associations_bidirectional_vehicles_cmlc import *

print("Bidirectional Associations (cmlc_py)\n")

donald = Employee.create_employee("Donald Duck", None, None)
corporation = Corporation.create_corporation("Walt Disney", [donald], [])
mouse = Vehicle.create_vehicle("MOUSE", None, corporation)
mickey = Employee.create_employee("Mickey Mouse", corporation, mouse)
duck = Vehicle.create_vehicle("DUCK", donald, corporation)

print(corporation)
print("- Employees: %s" % ', '.join(map(str, corporation.employees)))
print("- Fleet: %s" % ', '.join(map(str, corporation.fleet)))
print()

print(donald)
print("- Employer: %s" % donald.employer)
print("- Vehicle: %s" % donald.vehicle)
print()

print(mickey)
print("- Employer: %s" % mickey.employer)
print("- Vehicle: %s" % mickey.vehicle)
print()

print(duck)
print("- Owner: %s" % duck.owner)
print("- Driver: %s" % duck.driver)
print()

print(mouse)
print("- Owner: %s" % mouse.owner)
print("- Driver: %s" % mouse.driver)

