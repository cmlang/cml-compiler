from typing import *
from abc import *
from decimal import *

class _Membership:

    _singleton = None

    def __new__(cls) -> '_Membership':
        if cls._singleton is None:
            cls._singleton = super(_Membership, cls).__new__(cls)
        return cls._singleton

    def __init__(self) -> 'None':
        self.__parent = {}  # type: Dict[ModelElement, Optional[Scope]]
        self.__members = {}  # type: Dict[Scope, List[ModelElement]]

    def link_many(self, parent: 'Optional[Scope]', members: 'List[ModelElement]') -> 'None':
        for model_element in members: self.link(scope=parent, model_element=model_element)

    def link(self, scope: 'Scope', model_element: 'ModelElement') -> 'None':
        self.__parent[model_element] = scope

        if scope in self.__members:
            model_element_list = self.__members[scope]
        else:
            model_element_list = [model_element]
        if not (model_element in model_element_list):
            model_element_list.append(model_element)
        self.__members[scope] = model_element_list

    def parent_of(self, model_element: 'ModelElement') -> 'Optional[Scope]':
        if model_element in self.__parent:
            return self.__parent[model_element]
        else:
            return None

    def members_of(self, scope: 'Scope') -> 'List[ModelElement]':
        if scope in self.__members:
            model_element_list = self.__members[scope]
        else:
            model_element_list = []
        return list(model_element_list)


class _Localization:

    _singleton = None

    def __new__(cls) -> '_Localization':
        if cls._singleton is None:
            cls._singleton = super(_Localization, cls).__new__(cls)
        return cls._singleton

    def __init__(self) -> 'None':
        self.__element = {}  # type: Dict[Location, ModelElement]
        self.__location = {}  # type: Dict[ModelElement, Optional[Location]]

    def link(self, model_element: 'ModelElement', location: 'Location') -> 'None':
        self.__element[location] = model_element

        self.__location[model_element] = location

    def element_of(self, location: 'Location') -> 'Optional[ModelElement]':
        if location in self.__element:
            return self.__element[location]
        else:
            return None

    def location_of(self, model_element: 'ModelElement') -> 'Optional[Location]':
        if model_element in self.__location:
            return self.__location[model_element]
        else:
            return None


class ModelElement(ABC):

    @abstractproperty
    def parent(self) -> 'Optional[Scope]':
        pass

    @abstractproperty
    def location(self) -> 'Optional[Location]':
        pass

    @staticmethod
    def create_model_element(parent: 'Optional[Scope]', location: 'Optional[Location]') -> 'ModelElement':
        return ModelElementImpl(None, parent, location)

    @staticmethod
    def extend_model_element(actual_self: 'Optional[ModelElement]', parent: 'Optional[Scope]', location: 'Optional[Location]') -> 'ModelElement':
        return ModelElementImpl(actual_self, parent, location)


class ModelElementImpl(ModelElement):

    _membership = _Membership()
    _localization = _Localization()

    def __init__(self, actual_self: 'Optional[ModelElement]', parent: 'Optional[Scope]', location: 'Optional[Location]') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[ModelElement]
        else:
            self.__actual_self = actual_self


        self._membership.link(scope=parent, model_element=self.__actual_self)
        self._localization.link(location=location, model_element=self.__actual_self)

    @property
    def parent(self) -> 'Optional[Scope]':
        return self._membership.parent_of(self.__actual_self)

    @property
    def location(self) -> 'Optional[Location]':
        return self._localization.location_of(self.__actual_self)

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class Scope(ModelElement, ABC):

    @abstractproperty
    def members(self) -> 'List[ModelElement]':
        pass

    @staticmethod
    def create_scope(parent: 'Optional[Scope]', location: 'Optional[Location]', members: 'List[ModelElement]') -> 'Scope':
        return ScopeImpl(None, None, members, parent=parent, location=location)

    @staticmethod
    def extend_scope(actual_self: 'Optional[Scope]', model_element: 'ModelElement', members: 'List[ModelElement]') -> 'Scope':
        return ScopeImpl(actual_self, model_element, members)


class ScopeImpl(Scope):

    _membership = _Membership()

    def __init__(self, actual_self: 'Optional[Scope]', model_element: 'Optional[ModelElement]', members: 'List[ModelElement]', **kwargs) -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Scope]
        else:
            self.__actual_self = actual_self
        if model_element is None:
            self.__model_element = ModelElement.extend_model_element(self.__actual_self, kwargs['parent'], kwargs['location'])
        else:
            self.__model_element = model_element

        self._membership.link_many(self.__actual_self, members)

    @property
    def members(self) -> 'List[ModelElement]':
        return self._membership.members_of(self.__actual_self)

    @property
    def parent(self) -> 'Optional[Scope]':
        return self.__model_element.parent

    @property
    def location(self) -> 'Optional[Location]':
        return self.__model_element.location

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class Location(ABC):

    @abstractproperty
    def line(self) -> 'int':
        pass

    @abstractproperty
    def column(self) -> 'int':
        pass

    @abstractproperty
    def element(self) -> 'ModelElement':
        pass

    @staticmethod
    def create_location(line: 'int', column: 'int', element: 'ModelElement') -> 'Location':
        return LocationImpl(None, line, column, element)

    @staticmethod
    def extend_location(actual_self: 'Optional[Location]', line: 'int', column: 'int', element: 'ModelElement') -> 'Location':
        return LocationImpl(actual_self, line, column, element)


class LocationImpl(Location):

    _localization = _Localization()

    def __init__(self, actual_self: 'Optional[Location]', line: 'int', column: 'int', element: 'ModelElement') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Location]
        else:
            self.__actual_self = actual_self

        self.__line = line
        self.__column = column

        self._localization.link(model_element=element, location=self.__actual_self)

    @property
    def line(self) -> 'int':
        return self.__line

    @property
    def column(self) -> 'int':
        return self.__column

    @property
    def element(self) -> 'ModelElement':
        return self._localization.element_of(self.__actual_self)

    def __str__(self) -> 'str':
        return "%s(line=%s, column=%s)" % (
            type(self).__name__,
            self.line,
            self.column
        )


class NamedElement(ModelElement, ABC):

    @abstractproperty
    def name(self) -> 'str':
        pass

    @staticmethod
    def create_named_element(parent: 'Optional[Scope]', location: 'Optional[Location]', name: 'str') -> 'NamedElement':
        return NamedElementImpl(None, name, parent=parent, location=location)

    @staticmethod
    def extend_named_element(model_element: 'ModelElement', name: 'str') -> 'NamedElement':
        return NamedElementImpl(model_element, name)


class NamedElementImpl(NamedElement):

    def __init__(self, model_element: 'Optional[ModelElement]', name: 'str', **kwargs) -> 'None':
        if model_element is None:
            self.__model_element = ModelElement.extend_model_element(self, kwargs['parent'], kwargs['location'])
        else:
            self.__model_element = model_element
        self.__name = name

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def parent(self) -> 'Optional[Scope]':
        return self.__model_element.parent

    @property
    def location(self) -> 'Optional[Location]':
        return self.__model_element.location

    def __str__(self) -> 'str':
        return "%s(name=%s)" % (
            type(self).__name__,
            self.name
        )