
from expressions import *

anotherConcept = AnotherConcept(10.0)
anotherConcept2 = AnotherConcept(20.0)
someConcept = SomeConcept(-1, anotherConcept)
someConcept2 = SomeConcept(2, anotherConcept2)
cases = ExpressionCases("foo", someConcept, [someConcept, someConcept2])

print("Expressions Console\n")

print("LiteralStringInit = %s" % cases.literal_string_init)
print("LiteralIntegerInit = %d" % cases.literal_integer_init)
print("LiteralDecimalInit = %.3f" % cases.literal_decimal_init)
print()

print("self_var = " + str(cases.self_var))
print("single_var = " + cases.single_var)
print()

print("path_var = " + str(cases.path_var))
print("path_var_2 = " + str(cases.path_var_2))
print()

print("path_var_3 = " + str(cases.path_var_3))
print("path_bars = " + str(cases.path_bars))
