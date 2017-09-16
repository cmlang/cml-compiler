from typing import *
from abc import *
from decimal import *

class Location(ABC):

    @abstractproperty
    def line(self) -> 'int':
        pass

    @abstractproperty
    def column(self) -> 'int':
        pass

    @staticmethod
    def create_location(line: 'int', column: 'int') -> 'Location':
        return LocationImpl(line, column)

    @staticmethod
    def extend_location(line: 'int', column: 'int') -> 'Location':
        return LocationImpl(line, column)


class LocationImpl(Location):

    def __init__(self, line: 'int', column: 'int', **kwargs) -> 'None':
        
        self.__line = line
        self.__column = column

    @property
    def line(self) -> 'int':
        return self.__line

    @property
    def column(self) -> 'int':
        return self.__column

    def __str__(self) -> 'str':
        return "%s(line=%s, column=%s)" % (
            type(self).__name__,
            self.line,
            self.column
        )


class ModelElement(ABC):

    @abstractproperty
    def location(self) -> 'Location':
        pass

    @abstractproperty
    def parent(self) -> 'Scope':
        pass

    @staticmethod
    def extend_model_element(location: 'Optional[Location]', parent: 'Optional[Scope]') -> 'ModelElement':
        return ModelElementImpl(location, parent)


class ModelElementImpl(ModelElement):

    def __init__(self, location: 'Optional[Location]', parent: 'Optional[Scope]') -> 'None':
        
        self.__location = location
        self.__parent = parent

    @property
    def location(self) -> 'Location':
        return self.__location

    @property
    def parent(self) -> 'Scope':
        return self.__parent

    def __str__(self) -> 'str':
        return "%s(location=%s, parent=%s)" % (
            type(self).__name__,
            self.location,
            self.parent
        )


class Scope(ModelElement, ABC):

    @abstractproperty
    def members(self) -> 'List[ModelElement]':
        pass

    @staticmethod
    def extend_scope(model_element: 'ModelElement', members: 'List[ModelElement]') -> 'Scope':
        return ScopeImpl(model_element, members)


class ScopeImpl(Scope):

    def __init__(self, model_element: 'ModelElement', members: 'List[ModelElement]') -> 'None':
        self.__model_element = model_element
        self.__members = members

    @property
    def members(self) -> 'List[ModelElement]':
        return self.__members

    @property
    def location(self) -> 'Location':
        return self.__model_element.location

    @property
    def parent(self) -> 'Scope':
        return self.__model_element.parent

    def __str__(self) -> 'str':
        return "%s(location=%s, parent=%s)" % (
            type(self).__name__,
            self.location,
            self.parent
        )