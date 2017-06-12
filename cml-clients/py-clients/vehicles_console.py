
from vehicles import *

print("Vehicles Console")
print("----------------")
print()

organization = Organization("Walt Disney", [], [])
donald = Employee("Donald Duck", organization)
mickey = Employee("Mickey Mouse", organization)
duck = Vehicle("DUCK", donald, organization)
mouse = Vehicle("MOUSE", mickey, organization)

print(organization)
print("- Employees: [%s]" % ', '.join(map(str, organization.employees)))
print("- Fleet: [%s]" % ', '.join(map(str, organization.fleet)))
print()

print(donald)
print("- Employer: %s" % donald.employer)
print()

print(mickey)
print("- Employer: %s" % mickey.employer)
print()

print(duck)
print("- Owner: %s" % duck.owner)
print()

print(mouse)
print("- Owner: %s" % mouse.owner)
print()
