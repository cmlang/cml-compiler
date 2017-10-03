from typing import *
from abc import *
from decimal import *

import functools, itertools

class AnotherConcept:

    def __init__(self, etc: 'Decimal', flag: 'bool' = True) -> 'None':
        self.__etc = etc
        self.__flag = flag

    @property
    def etc(self) -> 'Decimal':
        return self.__etc

    @property
    def flag(self) -> 'bool':
        return self.__flag

    def __str__(self) -> 'str':
        return "%s(etc=%s, flag=%s)" % (
            type(self).__name__,
            self.etc,
            self.flag
        )


class SomeConcept:

    def __init__(self, bar: 'int', foos: 'List[AnotherConcept]', one_more_path: 'AnotherConcept') -> 'None':
        self.__bar = bar
        self.__foos = foos
        self.__one_more_path = one_more_path

    @property
    def bar(self) -> 'int':
        return self.__bar

    @property
    def foos(self) -> 'List[AnotherConcept]':
        return self.__foos

    @property
    def one_more_path(self) -> 'AnotherConcept':
        return self.__one_more_path

    def __str__(self) -> 'str':
        return "%s(bar=%s, one_more_path=%s)" % (
            type(self).__name__,
            self.bar,
            self.one_more_path
        )


class ExpressionCases:

    def __init__(self, foo: 'str', some_path: 'SomeConcept', some_path_list: 'List[SomeConcept]', opt_prop: 'Optional[AnotherConcept]') -> 'None':
        self.__foo = foo
        self.__some_path = some_path
        self.__some_path_list = some_path_list
        self.__opt_prop = opt_prop

    @property
    def foo(self) -> 'str':
        return self.__foo

    @property
    def some_path(self) -> 'SomeConcept':
        return self.__some_path

    @property
    def some_path_list(self) -> 'List[SomeConcept]':
        return self.__some_path_list

    @property
    def opt_prop(self) -> 'Optional[AnotherConcept]':
        return self.__opt_prop

    @property
    def self_var(self) -> 'ExpressionCases':
        return self

    @property
    def single_var(self) -> 'str':
        return self.foo

    @property
    def derived_some_path(self) -> 'SomeConcept':
        return self.some_path

    @property
    def path_var(self) -> 'int':
        return self.some_path.bar

    @property
    def path_var_2(self) -> 'Decimal':
        return self.some_path.one_more_path.etc

    @property
    def path_var_3(self) -> 'List[Decimal]':
        return list(
            map(
                lambda another_concept: another_concept.etc,
                map(
                    lambda some_concept: some_concept.one_more_path,
                    self.some_path_list
                )
            )
        )

    @property
    def path_bars(self) -> 'List[int]':
        return list(
            map(
                lambda some_concept: some_concept.bar,
                self.some_path_list
            )
        )

    @property
    def path_foos(self) -> 'List[AnotherConcept]':
        return list(
            itertools.chain.from_iterable(map(
                lambda some_concept: some_concept.foos,
                self.some_path_list
            ))
        )

    @property
    def sorted_list(self) -> 'List[SomeConcept]':
        return list(
            sorted(self.some_path_list, key=functools.cmp_to_key(lambda item_1, item_2: -1 if (item_1.bar < item_2.bar) else +1 if (item_2.bar < item_1.bar) else 0))
        )

    @property
    def opt_flag(self) -> 'bool':
        return False if self.opt_prop is None else self.opt_prop.flag

    def __str__(self) -> 'str':
        return "%s(foo=%s, some_path=%s, single_var=%s, path_var=%s, path_var_2=%s, path_var_3=%s, path_bars=%s, opt_prop=%s, opt_flag=%s)" % (
            type(self).__name__,
            self.foo,
            self.some_path,
            self.single_var,
            self.path_var,
            self.path_var_2,
            self.path_var_3,
            self.path_bars,
            self.opt_prop,
            self.opt_flag
        )