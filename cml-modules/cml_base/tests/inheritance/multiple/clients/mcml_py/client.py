
from cml_inheritance_multiple_mcml_cmlc import Model, Concept

model = Model.create_model(None, [])
concept = Concept.create_concept("SomeConcept", None, [], True)
print("Mini-CML Compiler")
print()
print(model)
print(concept)

