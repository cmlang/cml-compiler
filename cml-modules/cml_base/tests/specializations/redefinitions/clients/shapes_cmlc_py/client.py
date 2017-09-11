
from cml_specializations_redefinitions_shapes_cmlc import Rectangle, Rhombus, Square, Circle, UnitCircle, RedUnitCircle

print("Shapes Client (cmlc_py)\n")
print(Rectangle.create_rectangle("red", 10, 20))
print(Rhombus.create_rhombus("green", 14.1421356237, 14.1421356237))
print(Square.create_square("blue", 10))
print(Circle.create_circle(10, "red"))
print(UnitCircle.create_unit_circle())
print(RedUnitCircle.create_red_unit_circle())



