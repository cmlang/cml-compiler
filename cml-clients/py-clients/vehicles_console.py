
from vehicles import *

print("Vehicles Console")
print("----------------")
print()

organization = Organization("Walt Disney", [], [])
donald = Employee("Donald Duck", organization)
vehicle = Vehicle("DUCK", donald, organization)

print(organization)
print(donald)
print(vehicle)
