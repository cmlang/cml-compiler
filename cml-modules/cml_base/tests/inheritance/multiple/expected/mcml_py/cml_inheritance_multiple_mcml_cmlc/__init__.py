from typing import *
from abc import *
from decimal import *

import itertools

class ModelElement(ABC):

    @abstractproperty
    def parent(self) -> 'Optional[ModelElement]':
        pass

    @abstractproperty
    def elements(self) -> 'List[ModelElement]':
        pass

    @staticmethod
    def extend_model_element(parent: 'Optional[ModelElement]', elements: 'List[ModelElement]') -> 'ModelElement':
        return ModelElementImpl(parent, elements)


class ModelElementImpl(ModelElement):

    def __init__(self, parent: 'Optional[ModelElement]', elements: 'List[ModelElement]') -> 'None':
        
        self.__parent = parent
        self.__elements = elements

    @property
    def parent(self) -> 'Optional[ModelElement]':
        return self.__parent

    @property
    def elements(self) -> 'List[ModelElement]':
        return self.__elements

    def __str__(self) -> 'str':
        return "%s(parent=%s)" % (
            type(self).__name__,
            self.parent
        )


class NamedElement(ModelElement, ABC):

    @abstractproperty
    def name(self) -> 'str':
        pass

    @staticmethod
    def extend_named_element(model_element: 'ModelElement', name: 'str') -> 'NamedElement':
        return NamedElementImpl(model_element, name)


class NamedElementImpl(NamedElement):

    def __init__(self, model_element: 'ModelElement', name: 'str') -> 'None':
        self.__model_element = model_element
        self.__name = name

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def parent(self) -> 'Optional[ModelElement]':
        return self.__model_element.parent

    @property
    def elements(self) -> 'List[ModelElement]':
        return self.__model_element.elements

    def __str__(self) -> 'str':
        return "%s(name=%s, parent=%s)" % (
            type(self).__name__,
            self.name,
            self.parent
        )


class PropertyList(ModelElement, ABC):

    @staticmethod
    def extend_property_list(model_element: 'ModelElement') -> 'PropertyList':
        return PropertyListImpl(model_element)


class PropertyListImpl(PropertyList):

    def __init__(self, model_element: 'ModelElement') -> 'None':
        self.__model_element = model_element


    @property
    def parent(self) -> 'Optional[ModelElement]':
        return self.__model_element.parent

    @property
    def elements(self) -> 'List[ModelElement]':
        return self.__model_element.elements

    def __str__(self) -> 'str':
        return "%s(parent=%s)" % (
            type(self).__name__,
            self.parent
        )


class Concept(NamedElement, PropertyList, ABC):

    @abstractproperty
    def abstracted(self) -> 'bool':
        pass

    @staticmethod
    def create_concept(name: 'str', parent: 'Optional[ModelElement]', elements: 'List[ModelElement]', abstracted: 'bool') -> 'Concept':
        return ConceptImpl(None, None, None, abstracted, name=name, parent=parent, elements=elements)

    @staticmethod
    def extend_concept(model_element: 'ModelElement', named_element: 'NamedElement', property_list: 'PropertyList', abstracted: 'bool') -> 'Concept':
        return ConceptImpl(model_element, named_element, property_list, abstracted)


class ConceptImpl(Concept):

    def __init__(self, model_element: 'Optional[ModelElement]', named_element: 'Optional[NamedElement]', property_list: 'Optional[PropertyList]', abstracted: 'bool', **kwargs) -> 'None':
        if model_element is None:
            self.__model_element = ModelElement.extend_model_element(kwargs['parent'], kwargs['elements'])
        else:
            self.__model_element = model_element
        if named_element is None:
            self.__named_element = NamedElement.extend_named_element(self.__model_element, kwargs['name'])
        else:
            self.__named_element = named_element
        if property_list is None:
            self.__property_list = PropertyList.extend_property_list(self.__model_element)
        else:
            self.__property_list = property_list
        self.__abstracted = abstracted

    @property
    def abstracted(self) -> 'bool':
        return self.__abstracted

    @property
    def name(self) -> 'str':
        return self.__named_element.name

    @property
    def parent(self) -> 'Optional[ModelElement]':
        return self.__model_element.parent

    @property
    def elements(self) -> 'List[ModelElement]':
        return self.__model_element.elements

    def __str__(self) -> 'str':
        return "%s(abstracted=%s, name=%s, parent=%s)" % (
            type(self).__name__,
            self.abstracted,
            self.name,
            self.parent
        )


class Model(ModelElement, ABC):

    @staticmethod
    def create_model(parent: 'Optional[ModelElement]', elements: 'List[ModelElement]') -> 'Model':
        return ModelImpl(None, parent=parent, elements=elements)

    @staticmethod
    def extend_model(model_element: 'ModelElement') -> 'Model':
        return ModelImpl(model_element)


class ModelImpl(Model):

    def __init__(self, model_element: 'Optional[ModelElement]', **kwargs) -> 'None':
        if model_element is None:
            self.__model_element = ModelElement.extend_model_element(kwargs['parent'], kwargs['elements'])
        else:
            self.__model_element = model_element


    @property
    def parent(self) -> 'Optional[ModelElement]':
        return self.__model_element.parent

    @property
    def elements(self) -> 'List[ModelElement]':
        return self.__model_element.elements

    def __str__(self) -> 'str':
        return "%s(parent=%s)" % (
            type(self).__name__,
            self.parent
        )