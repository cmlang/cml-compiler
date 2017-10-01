
from cml_expressions_paths_pop import *

anotherConcept = AnotherConcept(10.0)
anotherConcept2 = AnotherConcept(20.0)
someConcept = SomeConcept(-1, [anotherConcept, anotherConcept2], anotherConcept)
someConcept2 = SomeConcept(2, [anotherConcept2, anotherConcept], anotherConcept2)
cases = ExpressionCases("foo", someConcept, [someConcept, someConcept2])

print("Paths Client (pop)\n")

print("self_var = " + str(cases.self_var))
print("single_var = " + cases.single_var)
print()

print("path_var = " + str(cases.path_var))
print("path_var_2 = " + str(cases.path_var_2))
print()

print("path_var_3 = " + str(cases.path_var_3))
print("path_bars = " + str(cases.path_bars))
print("path_foos = " + str(list(map(lambda item: str(item), cases.path_foos))))
