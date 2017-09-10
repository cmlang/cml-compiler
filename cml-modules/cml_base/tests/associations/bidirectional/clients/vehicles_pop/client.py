
from cml_associations_bidirectional_vehicles_pop import *

print("Bidirectional Associations (pop)\n")

corporation = Corporation("Walt Disney", [], [])
donald = Employee("Donald Duck", corporation)
mickey = Employee("Mickey Mouse", corporation)
duck = Vehicle("DUCK", donald, corporation)
mouse = Vehicle("MOUSE", mickey, corporation)

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
