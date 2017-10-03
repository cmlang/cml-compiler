from typing import *
from abc import *
from decimal import *

import functools, itertools

class Ancestor:

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class Descendant(Ancestor):

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class Types:

    def __init__(self, req: 'Ancestor', opt: 'Optional[Ancestor]', seq: 'List[Ancestor]') -> 'None':
        self.__req = req
        self.__opt = opt
        self.__seq = seq

    @property
    def req(self) -> 'Ancestor':
        return self.__req

    @property
    def opt(self) -> 'Optional[Ancestor]':
        return self.__opt

    @property
    def seq(self) -> 'List[Ancestor]':
        return self.__seq

    @property
    def type_check_is(self) -> 'bool':
        return isinstance(self.req, Descendant)

    @property
    def type_check_is_not(self) -> 'bool':
        return (not isinstance(self.req, Descendant))

    @property
    def req_to_req_type_cast_asb(self) -> 'Descendant':
        return cast('Descendant', self.req)

    @property
    def req_to_opt_type_cast_asb(self) -> 'Optional[Descendant]':
        return cast('Optional[Descendant]', self.req)

    @property
    def req_to_opt_type_cast_asq(self) -> 'Optional[Descendant]':
        return cast('Descendant', self.req) if isinstance(self.req, Descendant) else None

    @property
    def opt_to_opt_type_cast_asb(self) -> 'Optional[Descendant]':
        return cast('Optional[Descendant]', self.opt)

    @property
    def opt_to_opt_type_cast_asq(self) -> 'Optional[Descendant]':
        return cast('Descendant', self.opt) if isinstance(self.opt, Descendant) else None

    @property
    def req_to_seq_type_cast_asb(self) -> 'List[Descendant]':
        return list(
            [cast('Descendant', self.req)]
        )

    @property
    def opt_to_seq_type_cast_asb(self) -> 'List[Descendant]':
        return list(
            [] if self.opt is None else [cast('Descendant', self.opt)]
        )

    @property
    def seq_to_seq_type_cast_asb(self) -> 'List[Descendant]':
        return list(
            map(lambda item: cast('Descendant', item), self.seq)
        )

    @property
    def req_to_seq_type_cast_asq(self) -> 'List[Descendant]':
        return list(
            map(lambda item: cast('Descendant', item), filter(lambda item: isinstance(item, Descendant), [self.req]))
        )

    @property
    def opt_to_seq_type_cast_asq(self) -> 'List[Descendant]':
        return list(
            map(lambda item: cast('Descendant', item), filter(lambda item: isinstance(item, Descendant), [self.opt]))
        )

    @property
    def seq_to_seq_type_cast_asq(self) -> 'List[Descendant]':
        return list(
            map(lambda item: cast('Descendant', item), filter(lambda item: isinstance(item, Descendant), self.seq))
        )

    @property
    def descendants(self) -> 'List[Ancestor]':
        return list(
            filter(lambda a: isinstance(a, Descendant), self.seq)
        )

    def __str__(self) -> 'str':
        return "%s(req=%s, opt=%s, type_check_is=%s, type_check_is_not=%s)" % (
            type(self).__name__,
            self.req,
            self.opt,
            self.type_check_is,
            self.type_check_is_not
        )