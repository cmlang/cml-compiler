
from cml_expressions_paths_pop import *

anotherConcept = AnotherConcept(Etc())
anotherConcept2 = AnotherConcept(Etc())
someConcept = SomeConcept(-1, Bar(), [anotherConcept, anotherConcept2], anotherConcept)
someConcept2 = SomeConcept(2, Bar(), [anotherConcept2, anotherConcept], anotherConcept2)
cases = ExpressionCases("foo", someConcept, [someConcept2, someConcept], None)

print("Paths Client (pop)\n")

print("self_var = " + str(cases.self_var))
print("single_var = " + cases.single_var)
print()

print("path_var = " + str(cases.path_var))
print("path_var_2 = " + str(cases.path_var_2))
print()

print("path_var_3 = " + str(list(map(lambda item: str(item), cases.path_var_3))))
print("path_bars = " + str(list(map(lambda item: str(item), cases.path_bars))))
print("path_foos = " + str(list(map(lambda item: str(item), cases.path_foos))))

print("some_path_list = " + str(list(map(lambda item: str(item), cases.some_path_list))))
print("sorted_list = " + str(list(map(lambda item: str(item), cases.sorted_list))))
