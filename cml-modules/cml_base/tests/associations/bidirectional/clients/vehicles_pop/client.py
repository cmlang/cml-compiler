
from cml_associations_bidirectional_vehicles_pop import *

print("Bidirectional Associations (pop)\n")

organization = Organization("Walt Disney", [], [])
donald = Employee("Donald Duck", organization)
mickey = Employee("Mickey Mouse", organization)
duck = Vehicle("DUCK", donald, organization)
mouse = Vehicle("MOUSE", mickey, organization)

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
