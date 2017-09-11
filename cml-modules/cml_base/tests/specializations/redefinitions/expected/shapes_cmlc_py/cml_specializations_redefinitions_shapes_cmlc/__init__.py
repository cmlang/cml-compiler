from typing import *
from abc import *
from decimal import *

class Shape(ABC):

    @abstractproperty
    def color(self) -> 'str':
        pass

    @abstractproperty
    def area(self) -> 'float':
        pass

    @staticmethod
    def extend_shape(color: 'str') -> 'Shape':
        return ShapeImpl(color)


class ShapeImpl(Shape):

    def __init__(self, color: 'str') -> 'None':
        
        self.__color = color

    @property
    def color(self) -> 'str':
        return self.__color

    @property
    def area(self) -> 'float':
        return 0

    def __str__(self) -> 'str':
        return "%s(color=%s, area=%s)" % (
            type(self).__name__,
            self.color,
            self.area
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
        return RectangleImpl(None, width, height, color=color)

    @staticmethod
    def extend_rectangle(shape: 'Shape', width: 'float', height: 'float') -> 'Rectangle':
        return RectangleImpl(shape, width, height)


class RectangleImpl(Rectangle):

    def __init__(self, shape: 'Optional[Shape]', width: 'float', height: 'float', **kwargs) -> 'None':
        if shape is None:
            self.__shape = Shape.extend_shape(kwargs['color'])
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
        return (self.width * self.height)

    @property
    def color(self) -> 'str':
        return self.__shape.color

    def __str__(self) -> 'str':
        return "%s(width=%s, height=%s, area=%s, color=%s)" % (
            type(self).__name__,
            self.width,
            self.height,
            self.area,
            self.color
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
        return RhombusImpl(None, p, q, color=color)

    @staticmethod
    def extend_rhombus(shape: 'Shape', p: 'float', q: 'float') -> 'Rhombus':
        return RhombusImpl(shape, p, q)


class RhombusImpl(Rhombus):

    def __init__(self, shape: 'Optional[Shape]', p: 'float', q: 'float', **kwargs) -> 'None':
        if shape is None:
            self.__shape = Shape.extend_shape(kwargs['color'])
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
        return ((self.p * self.q) / 2.0)

    @property
    def color(self) -> 'str':
        return self.__shape.color

    def __str__(self) -> 'str':
        return "%s(p=%s, q=%s, area=%s, color=%s)" % (
            type(self).__name__,
            self.p,
            self.q,
            self.area,
            self.color
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
        return SquareImpl(None, None, None, side_length, color=color)

    @staticmethod
    def extend_square(shape: 'Shape', rectangle: 'Rectangle', rhombus: 'Rhombus', side_length: 'float') -> 'Square':
        return SquareImpl(shape, rectangle, rhombus, side_length)


class SquareImpl(Square):

    def __init__(self, shape: 'Optional[Shape]', rectangle: 'Optional[Rectangle]', rhombus: 'Optional[Rhombus]', side_length: 'float', **kwargs) -> 'None':
        if shape is None:
            self.__shape = Shape.extend_shape(kwargs['color'])
        else:
            self.__shape = shape
        if rectangle is None:
            self.__rectangle = Rectangle.extend_rectangle(self.__shape, 0, 0)
        else:
            self.__rectangle = rectangle
        if rhombus is None:
            self.__rhombus = Rhombus.extend_rhombus(self.__shape, 0, 0)
        else:
            self.__rhombus = rhombus
        self.__side_length = side_length

    @property
    def side_length(self) -> 'float':
        return self.__side_length

    @property
    def width(self) -> 'float':
        return self.side_length

    @property
    def height(self) -> 'float':
        return self.side_length

    @property
    def p(self) -> 'float':
        return (self.side_length * 1.41421356237)

    @property
    def q(self) -> 'float':
        return self.p

    @property
    def area(self) -> 'float':
        return (self.side_length ** 2.0)

    @property
    def color(self) -> 'str':
        return self.__shape.color

    def __str__(self) -> 'str':
        return "%s(side_length=%s, width=%s, height=%s, p=%s, q=%s, area=%s, color=%s)" % (
            type(self).__name__,
            self.side_length,
            self.width,
            self.height,
            self.p,
            self.q,
            self.area,
            self.color
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
        return CircleImpl(None, radius, color)

    @staticmethod
    def extend_circle(shape: 'Shape', radius: 'float', color: 'str' = "Blue") -> 'Circle':
        return CircleImpl(shape, radius, color)


class CircleImpl(Circle):

    def __init__(self, shape: 'Optional[Shape]', radius: 'float', color: 'str' = "Blue", **kwargs) -> 'None':
        if shape is None:
            self.__shape = Shape.extend_shape(color)
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
        return (3.14159 * (self.radius ** 2.0))

    def __str__(self) -> 'str':
        return "%s(radius=%s, area=%s, color=%s)" % (
            type(self).__name__,
            self.radius,
            self.area,
            self.color
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
        return UnitCircleImpl(None, None, area, color=color)

    @staticmethod
    def extend_unit_circle(shape: 'Shape', circle: 'Circle', area: 'float' = 3.14159) -> 'UnitCircle':
        return UnitCircleImpl(shape, circle, area)


class UnitCircleImpl(UnitCircle):

    def __init__(self, shape: 'Optional[Shape]', circle: 'Optional[Circle]', area: 'float' = 3.14159, **kwargs) -> 'None':
        if shape is None:
            self.__shape = Shape.extend_shape(kwargs['color'])
        else:
            self.__shape = shape
        if circle is None:
            self.__circle = Circle.extend_circle(self.__shape, 0, kwargs['color'])
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

    def __str__(self) -> 'str':
        return "%s(area=%s, radius=%s, color=%s)" % (
            type(self).__name__,
            self.area,
            self.radius,
            self.color
        )


class RedUnitCircle(UnitCircle, ABC):

    @abstractproperty
    def color(self) -> 'str':
        pass

    @staticmethod
    def create_red_unit_circle(area: 'float' = 3.14159, color: 'str' = "Red") -> 'RedUnitCircle':
        return RedUnitCircleImpl(None, None, None, color, area=area)

    @staticmethod
    def extend_red_unit_circle(shape: 'Shape', circle: 'Circle', unit_circle: 'UnitCircle', color: 'str' = "Red") -> 'RedUnitCircle':
        return RedUnitCircleImpl(shape, circle, unit_circle, color)


class RedUnitCircleImpl(RedUnitCircle):

    def __init__(self, shape: 'Optional[Shape]', circle: 'Optional[Circle]', unit_circle: 'Optional[UnitCircle]', color: 'str' = "Red", **kwargs) -> 'None':
        if shape is None:
            self.__shape = Shape.extend_shape(color)
        else:
            self.__shape = shape
        if circle is None:
            self.__circle = Circle.extend_circle(self.__shape, 0, color)
        else:
            self.__circle = circle
        if unit_circle is None:
            self.__unit_circle = UnitCircle.extend_unit_circle(self.__shape, self.__circle, kwargs['area'])
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

    def __str__(self) -> 'str':
        return "%s(color=%s, area=%s, radius=%s)" % (
            type(self).__name__,
            self.color,
            self.area,
            self.radius
        )