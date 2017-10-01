from typing import *
from abc import *
from decimal import *

import itertools

class Shape(ABC):

    @abstractproperty
    def color(self) -> 'str':
        pass

    @abstractproperty
    def area(self) -> 'float':
        pass

    @abstractproperty
    def total_area(self) -> 'float':
        pass

    @staticmethod
    def extend_shape(actual_self: 'Optional[Shape]', color: 'str') -> 'Shape':
        return ShapeImpl(actual_self, color)


class ShapeImpl(Shape):

    def __init__(self, actual_self: 'Optional[Shape]', color: 'str') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Shape]
        else:
            self.__actual_self = actual_self

        self.__color = color

    @property
    def color(self) -> 'str':
        return self.__color

    @property
    def area(self) -> 'float':
        return 0

    @property
    def total_area(self) -> 'float':
        return self.__actual_self.area

    def __str__(self) -> 'str':
        return "%s(color=%s, area=%s, total_area=%s)" % (
            type(self).__name__,
            self.__actual_self.color,
            self.__actual_self.area,
            self.__actual_self.total_area
        )


class Rectangle(Shape, ABC):

    @abstractproperty
    def width(self) -> 'float':
        pass

    @abstractproperty
    def height(self) -> 'float':
        pass

    @abstractproperty
    def area(self) -> 'float':
        pass

    @staticmethod
    def create_rectangle(color: 'str', width: 'float', height: 'float') -> 'Rectangle':
        return RectangleImpl(None, None, width, height, color=color)

    @staticmethod
    def extend_rectangle(actual_self: 'Optional[Rectangle]', shape: 'Shape', width: 'float', height: 'float') -> 'Rectangle':
        return RectangleImpl(actual_self, shape, width, height)


class RectangleImpl(Rectangle):

    def __init__(self, actual_self: 'Optional[Rectangle]', shape: 'Optional[Shape]', width: 'float', height: 'float', **kwargs) -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Rectangle]
        else:
            self.__actual_self = actual_self

        if shape is None:
            self.__shape = Shape.extend_shape(self.__actual_self, kwargs['color'])
        else:
            self.__shape = shape
        self.__width = width
        self.__height = height

    @property
    def width(self) -> 'float':
        return self.__width

    @property
    def height(self) -> 'float':
        return self.__height

    @property
    def area(self) -> 'float':
        return (self.__actual_self.width * self.__actual_self.height)

    @property
    def color(self) -> 'str':
        return self.__shape.color

    @property
    def total_area(self) -> 'float':
        return self.__shape.total_area

    def __str__(self) -> 'str':
        return "%s(width=%s, height=%s, area=%s, color=%s, total_area=%s)" % (
            type(self).__name__,
            self.__actual_self.width,
            self.__actual_self.height,
            self.__actual_self.area,
            self.__actual_self.color,
            self.__actual_self.total_area
        )


class Rhombus(Shape, ABC):

    @abstractproperty
    def p(self) -> 'float':
        pass

    @abstractproperty
    def q(self) -> 'float':
        pass

    @abstractproperty
    def area(self) -> 'float':
        pass

    @staticmethod
    def create_rhombus(color: 'str', p: 'float', q: 'float') -> 'Rhombus':
        return RhombusImpl(None, None, p, q, color=color)

    @staticmethod
    def extend_rhombus(actual_self: 'Optional[Rhombus]', shape: 'Shape', p: 'float', q: 'float') -> 'Rhombus':
        return RhombusImpl(actual_self, shape, p, q)


class RhombusImpl(Rhombus):

    def __init__(self, actual_self: 'Optional[Rhombus]', shape: 'Optional[Shape]', p: 'float', q: 'float', **kwargs) -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Rhombus]
        else:
            self.__actual_self = actual_self

        if shape is None:
            self.__shape = Shape.extend_shape(self.__actual_self, kwargs['color'])
        else:
            self.__shape = shape
        self.__p = p
        self.__q = q

    @property
    def p(self) -> 'float':
        return self.__p

    @property
    def q(self) -> 'float':
        return self.__q

    @property
    def area(self) -> 'float':
        return ((self.__actual_self.p * self.__actual_self.q) / 2.0)

    @property
    def color(self) -> 'str':
        return self.__shape.color

    @property
    def total_area(self) -> 'float':
        return self.__shape.total_area

    def __str__(self) -> 'str':
        return "%s(p=%s, q=%s, area=%s, color=%s, total_area=%s)" % (
            type(self).__name__,
            self.__actual_self.p,
            self.__actual_self.q,
            self.__actual_self.area,
            self.__actual_self.color,
            self.__actual_self.total_area
        )


class Square(Rectangle, Rhombus, ABC):

    @abstractproperty
    def side_length(self) -> 'float':
        pass

    @abstractproperty
    def width(self) -> 'float':
        pass

    @abstractproperty
    def height(self) -> 'float':
        pass

    @abstractproperty
    def p(self) -> 'float':
        pass

    @abstractproperty
    def q(self) -> 'float':
        pass

    @abstractproperty
    def area(self) -> 'float':
        pass

    @staticmethod
    def create_square(color: 'str', side_length: 'float') -> 'Square':
        return SquareImpl(None, None, None, None, side_length, color=color)

    @staticmethod
    def extend_square(actual_self: 'Optional[Square]', shape: 'Shape', rectangle: 'Rectangle', rhombus: 'Rhombus', side_length: 'float') -> 'Square':
        return SquareImpl(actual_self, shape, rectangle, rhombus, side_length)


class SquareImpl(Square):

    def __init__(self, actual_self: 'Optional[Square]', shape: 'Optional[Shape]', rectangle: 'Optional[Rectangle]', rhombus: 'Optional[Rhombus]', side_length: 'float', **kwargs) -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Square]
        else:
            self.__actual_self = actual_self

        if shape is None:
            self.__shape = Shape.extend_shape(self.__actual_self, kwargs['color'])
        else:
            self.__shape = shape
        if rectangle is None:
            self.__rectangle = Rectangle.extend_rectangle(self.__actual_self, self.__shape, 0, 0)
        else:
            self.__rectangle = rectangle
        if rhombus is None:
            self.__rhombus = Rhombus.extend_rhombus(self.__actual_self, self.__shape, 0, 0)
        else:
            self.__rhombus = rhombus
        self.__side_length = side_length

    @property
    def side_length(self) -> 'float':
        return self.__side_length

    @property
    def width(self) -> 'float':
        return self.__actual_self.side_length

    @property
    def height(self) -> 'float':
        return self.__actual_self.side_length

    @property
    def p(self) -> 'float':
        return (self.__actual_self.side_length * 1.41421356237)

    @property
    def q(self) -> 'float':
        return self.__actual_self.p

    @property
    def area(self) -> 'float':
        return (self.__actual_self.side_length ** 2.0)

    @property
    def color(self) -> 'str':
        return self.__shape.color

    @property
    def total_area(self) -> 'float':
        return self.__shape.total_area

    def __str__(self) -> 'str':
        return "%s(side_length=%s, width=%s, height=%s, p=%s, q=%s, area=%s, color=%s, total_area=%s)" % (
            type(self).__name__,
            self.__actual_self.side_length,
            self.__actual_self.width,
            self.__actual_self.height,
            self.__actual_self.p,
            self.__actual_self.q,
            self.__actual_self.area,
            self.__actual_self.color,
            self.__actual_self.total_area
        )


class Circle(Shape, ABC):

    @abstractproperty
    def radius(self) -> 'float':
        pass

    @abstractproperty
    def area(self) -> 'float':
        pass

    @abstractproperty
    def color(self) -> 'str':
        pass

    @staticmethod
    def create_circle(radius: 'float', color: 'str' = "Blue") -> 'Circle':
        return CircleImpl(None, None, radius, color)

    @staticmethod
    def extend_circle(actual_self: 'Optional[Circle]', shape: 'Shape', radius: 'float', color: 'str' = "Blue") -> 'Circle':
        return CircleImpl(actual_self, shape, radius, color)


class CircleImpl(Circle):

    def __init__(self, actual_self: 'Optional[Circle]', shape: 'Optional[Shape]', radius: 'float', color: 'str' = "Blue") -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Circle]
        else:
            self.__actual_self = actual_self

        if shape is None:
            self.__shape = Shape.extend_shape(self.__actual_self, color)
        else:
            self.__shape = shape
        self.__radius = radius
        self.__color = color

    @property
    def radius(self) -> 'float':
        return self.__radius

    @property
    def color(self) -> 'str':
        return self.__color

    @property
    def area(self) -> 'float':
        return (3.14159 * (self.__actual_self.radius ** 2.0))

    @property
    def total_area(self) -> 'float':
        return self.__shape.total_area

    def __str__(self) -> 'str':
        return "%s(radius=%s, area=%s, color=%s, total_area=%s)" % (
            type(self).__name__,
            self.__actual_self.radius,
            self.__actual_self.area,
            self.__actual_self.color,
            self.__actual_self.total_area
        )


class UnitCircle(Circle, ABC):

    @abstractproperty
    def area(self) -> 'float':
        pass

    @abstractproperty
    def radius(self) -> 'float':
        pass

    @staticmethod
    def create_unit_circle(color: 'str' = "Blue", area: 'float' = 3.14159) -> 'UnitCircle':
        return UnitCircleImpl(None, None, None, area, color=color)

    @staticmethod
    def extend_unit_circle(actual_self: 'Optional[UnitCircle]', shape: 'Shape', circle: 'Circle', area: 'float' = 3.14159) -> 'UnitCircle':
        return UnitCircleImpl(actual_self, shape, circle, area)


class UnitCircleImpl(UnitCircle):

    def __init__(self, actual_self: 'Optional[UnitCircle]', shape: 'Optional[Shape]', circle: 'Optional[Circle]', area: 'float' = 3.14159, **kwargs) -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[UnitCircle]
        else:
            self.__actual_self = actual_self

        if shape is None:
            self.__shape = Shape.extend_shape(self.__actual_self, kwargs['color'])
        else:
            self.__shape = shape
        if circle is None:
            self.__circle = Circle.extend_circle(self.__actual_self, self.__shape, 0, kwargs['color'])
        else:
            self.__circle = circle
        self.__area = area

    @property
    def area(self) -> 'float':
        return self.__area

    @property
    def radius(self) -> 'float':
        return 1.0

    @property
    def color(self) -> 'str':
        return self.__circle.color

    @property
    def total_area(self) -> 'float':
        return self.__shape.total_area

    def __str__(self) -> 'str':
        return "%s(area=%s, radius=%s, color=%s, total_area=%s)" % (
            type(self).__name__,
            self.__actual_self.area,
            self.__actual_self.radius,
            self.__actual_self.color,
            self.__actual_self.total_area
        )


class RedUnitCircle(UnitCircle, ABC):

    @abstractproperty
    def color(self) -> 'str':
        pass

    @staticmethod
    def create_red_unit_circle(area: 'float' = 3.14159, color: 'str' = "Red") -> 'RedUnitCircle':
        return RedUnitCircleImpl(None, None, None, None, color, area=area)

    @staticmethod
    def extend_red_unit_circle(actual_self: 'Optional[RedUnitCircle]', shape: 'Shape', circle: 'Circle', unit_circle: 'UnitCircle', color: 'str' = "Red") -> 'RedUnitCircle':
        return RedUnitCircleImpl(actual_self, shape, circle, unit_circle, color)


class RedUnitCircleImpl(RedUnitCircle):

    def __init__(self, actual_self: 'Optional[RedUnitCircle]', shape: 'Optional[Shape]', circle: 'Optional[Circle]', unit_circle: 'Optional[UnitCircle]', color: 'str' = "Red", **kwargs) -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[RedUnitCircle]
        else:
            self.__actual_self = actual_self

        if shape is None:
            self.__shape = Shape.extend_shape(self.__actual_self, color)
        else:
            self.__shape = shape
        if circle is None:
            self.__circle = Circle.extend_circle(self.__actual_self, self.__shape, 0, color)
        else:
            self.__circle = circle
        if unit_circle is None:
            self.__unit_circle = UnitCircle.extend_unit_circle(self.__actual_self, self.__shape, self.__circle, kwargs['area'])
        else:
            self.__unit_circle = unit_circle
        self.__color = color

    @property
    def color(self) -> 'str':
        return self.__color

    @property
    def area(self) -> 'float':
        return self.__unit_circle.area

    @property
    def radius(self) -> 'float':
        return self.__unit_circle.radius

    @property
    def total_area(self) -> 'float':
        return self.__shape.total_area

    def __str__(self) -> 'str':
        return "%s(color=%s, area=%s, radius=%s, total_area=%s)" % (
            type(self).__name__,
            self.__actual_self.color,
            self.__actual_self.area,
            self.__actual_self.radius,
            self.__actual_self.total_area
        )