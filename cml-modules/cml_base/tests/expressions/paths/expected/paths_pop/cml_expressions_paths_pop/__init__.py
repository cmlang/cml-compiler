from typing import *
from abc import *
from decimal import *

import itertools

class AnotherConcept:

    def __init__(self, etc: 'Decimal') -> 'None':
        self.__etc = etc

    @property
    def etc(self) -> 'Decimal':
        return self.__etc

    def __str__(self) -> 'str':
        return "%s(etc=%s)" % (
            type(self).__name__,
            self.etc
        )


class SomeConcept:

    def __init__(self, bar: 'int', one_more_path: 'AnotherConcept') -> 'None':
        self.__bar = bar
        self.__one_more_path = one_more_path

    @property
    def bar(self) -> 'int':
        return self.__bar

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

    def __init__(self, foo: 'str', some_path: 'SomeConcept', some_path_list: 'List[SomeConcept]') -> 'None':
        self.__foo = foo
        self.__some_path = some_path
        self.__some_path_list = some_path_list

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
    def self_var(self) -> 'ExpressionCases':
        return self

    @property
    def single_var(self) -> 'str':
        return self.foo

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

    def __str__(self) -> 'str':
        return "%s(foo=%s, some_path=%s, single_var=%s, path_var=%s, path_var_2=%s, path_var_3=%s, path_bars=%s)" % (
            type(self).__name__,
            self.foo,
            self.some_path,
            self.single_var,
            self.path_var,
            self.path_var_2,
            self.path_var_3,
            self.path_bars
        )