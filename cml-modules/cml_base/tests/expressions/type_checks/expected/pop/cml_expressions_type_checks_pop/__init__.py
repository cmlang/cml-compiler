from typing import *
from abc import *
from decimal import *

import itertools

class Ancestor:

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class TypeChecks:

    def __init__(self, ancestors: 'List[Ancestor]') -> 'None':
        self.__ancestors = ancestors

    @property
    def ancestors(self) -> 'List[Ancestor]':
        return self.__ancestors

    @property
    def type_check_is(self) -> 'bool':
        return isinstance(self, TypeChecks)

    @property
    def type_check_is_not(self) -> 'bool':
        return (not isinstance(self, TypeChecks))

    @property
    def descendants(self) -> 'List[Ancestor]':
        return list(
            filter(lambda a: isinstance(a, Descendant), self.ancestors)
        )

    def __str__(self) -> 'str':
        return "%s(type_check_is=%s, type_check_is_not=%s)" % (
            type(self).__name__,
            self.type_check_is,
            self.type_check_is_not
        )


class Descendant(Ancestor):

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__