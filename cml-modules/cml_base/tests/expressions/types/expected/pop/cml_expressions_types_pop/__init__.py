from typing import *
from abc import *
from decimal import *

import itertools

class Ancestor:

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class Descendant(Ancestor):

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class Types:

    def __init__(self, single_ancestor: 'Ancestor', ancestors: 'List[Ancestor]') -> 'None':
        self.__single_ancestor = single_ancestor
        self.__ancestors = ancestors

    @property
    def single_ancestor(self) -> 'Ancestor':
        return self.__single_ancestor

    @property
    def ancestors(self) -> 'List[Ancestor]':
        return self.__ancestors

    @property
    def type_check_is(self) -> 'bool':
        return isinstance(self.single_ancestor, Descendant)

    @property
    def type_check_is_not(self) -> 'bool':
        return (not isinstance(self.single_ancestor, Descendant))

    @property
    def type_cast(self) -> 'Descendant':
        return cast('Descendant', self.single_ancestor)

    @property
    def descendants(self) -> 'List[Ancestor]':
        return list(
            filter(lambda a: isinstance(a, Descendant), self.ancestors)
        )

    def __str__(self) -> 'str':
        return "%s(single_ancestor=%s, type_check_is=%s, type_check_is_not=%s)" % (
            type(self).__name__,
            self.single_ancestor,
            self.type_check_is,
            self.type_check_is_not
        )