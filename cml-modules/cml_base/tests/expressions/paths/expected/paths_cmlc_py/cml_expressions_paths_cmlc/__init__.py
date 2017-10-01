from typing import *
from abc import *
from decimal import *

import itertools

class AnotherConcept(ABC):

    @abstractproperty
    def etc(self) -> 'Decimal':
        pass

    @staticmethod
    def create_another_concept(etc: 'Decimal') -> 'AnotherConcept':
        return AnotherConceptImpl(None, etc)

    @staticmethod
    def extend_another_concept(actual_self: 'Optional[AnotherConcept]', etc: 'Decimal') -> 'AnotherConcept':
        return AnotherConceptImpl(actual_self, etc)


class AnotherConceptImpl(AnotherConcept):

    def __init__(self, actual_self: 'Optional[AnotherConcept]', etc: 'Decimal') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[AnotherConcept]
        else:
            self.__actual_self = actual_self

        self.__etc = etc

    @property
    def etc(self) -> 'Decimal':
        return self.__etc

    def __str__(self) -> 'str':
        return "%s(etc=%s)" % (
            type(self).__name__,
            self.__actual_self.etc
        )


class SomeConcept(ABC):

    @abstractproperty
    def bar(self) -> 'int':
        pass

    @abstractproperty
    def foos(self) -> 'List[AnotherConcept]':
        pass

    @abstractproperty
    def one_more_path(self) -> 'AnotherConcept':
        pass

    @staticmethod
    def create_some_concept(bar: 'int', foos: 'List[AnotherConcept]', one_more_path: 'AnotherConcept') -> 'SomeConcept':
        return SomeConceptImpl(None, bar, foos, one_more_path)

    @staticmethod
    def extend_some_concept(actual_self: 'Optional[SomeConcept]', bar: 'int', foos: 'List[AnotherConcept]', one_more_path: 'AnotherConcept') -> 'SomeConcept':
        return SomeConceptImpl(actual_self, bar, foos, one_more_path)


class SomeConceptImpl(SomeConcept):

    def __init__(self, actual_self: 'Optional[SomeConcept]', bar: 'int', foos: 'List[AnotherConcept]', one_more_path: 'AnotherConcept') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[SomeConcept]
        else:
            self.__actual_self = actual_self

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
            self.__actual_self.bar,
            self.__actual_self.one_more_path
        )


class ExpressionCases(ABC):

    @abstractproperty
    def foo(self) -> 'str':
        pass

    @abstractproperty
    def some_path(self) -> 'SomeConcept':
        pass

    @abstractproperty
    def some_path_list(self) -> 'List[SomeConcept]':
        pass

    @abstractproperty
    def self_var(self) -> 'ExpressionCases':
        pass

    @abstractproperty
    def single_var(self) -> 'str':
        pass

    @abstractproperty
    def derived_some_path(self) -> 'SomeConcept':
        pass

    @abstractproperty
    def path_var(self) -> 'int':
        pass

    @abstractproperty
    def path_var_2(self) -> 'Decimal':
        pass

    @abstractproperty
    def path_var_3(self) -> 'List[Decimal]':
        pass

    @abstractproperty
    def path_bars(self) -> 'List[int]':
        pass

    @abstractproperty
    def path_foos(self) -> 'List[AnotherConcept]':
        pass

    @staticmethod
    def create_expression_cases(foo: 'str', some_path: 'SomeConcept', some_path_list: 'List[SomeConcept]') -> 'ExpressionCases':
        return ExpressionCasesImpl(None, foo, some_path, some_path_list)

    @staticmethod
    def extend_expression_cases(actual_self: 'Optional[ExpressionCases]', foo: 'str', some_path: 'SomeConcept', some_path_list: 'List[SomeConcept]') -> 'ExpressionCases':
        return ExpressionCasesImpl(actual_self, foo, some_path, some_path_list)


class ExpressionCasesImpl(ExpressionCases):

    def __init__(self, actual_self: 'Optional[ExpressionCases]', foo: 'str', some_path: 'SomeConcept', some_path_list: 'List[SomeConcept]') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[ExpressionCases]
        else:
            self.__actual_self = actual_self

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
        return self.__actual_self

    @property
    def single_var(self) -> 'str':
        return self.__actual_self.foo

    @property
    def derived_some_path(self) -> 'SomeConcept':
        return self.__actual_self.some_path

    @property
    def path_var(self) -> 'int':
        return self.__actual_self.some_path.bar

    @property
    def path_var_2(self) -> 'Decimal':
        return self.__actual_self.some_path.one_more_path.etc

    @property
    def path_var_3(self) -> 'List[Decimal]':
        return list(
            map(
                lambda another_concept: another_concept.etc,
                map(
                    lambda some_concept: some_concept.one_more_path,
                    self.__actual_self.some_path_list
                )
            )
        )

    @property
    def path_bars(self) -> 'List[int]':
        return list(
            map(
                lambda some_concept: some_concept.bar,
                self.__actual_self.some_path_list
            )
        )

    @property
    def path_foos(self) -> 'List[AnotherConcept]':
        return list(
            itertools.chain.from_iterable(map(
                lambda some_concept: some_concept.foos,
                self.__actual_self.some_path_list
            ))
        )

    def __str__(self) -> 'str':
        return "%s(foo=%s, some_path=%s, single_var=%s, path_var=%s, path_var_2=%s, path_var_3=%s, path_bars=%s)" % (
            type(self).__name__,
            self.__actual_self.foo,
            self.__actual_self.some_path,
            self.__actual_self.single_var,
            self.__actual_self.path_var,
            self.__actual_self.path_var_2,
            self.__actual_self.path_var_3,
            self.__actual_self.path_bars
        )