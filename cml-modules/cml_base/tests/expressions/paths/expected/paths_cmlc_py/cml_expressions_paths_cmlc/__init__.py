from typing import *
from abc import *
from decimal import *

import functools, itertools

class AnotherConcept(ABC):

    @abstractproperty
    def etc(self) -> 'Etc':
        pass

    @abstractproperty
    def flag(self) -> 'bool':
        pass

    @staticmethod
    def create_another_concept(etc: 'Etc', flag: 'bool' = True) -> 'AnotherConcept':
        return AnotherConceptImpl(None, etc, flag)

    @staticmethod
    def extend_another_concept(actual_self: 'Optional[AnotherConcept]', etc: 'Etc', flag: 'bool' = True) -> 'AnotherConcept':
        return AnotherConceptImpl(actual_self, etc, flag)


class AnotherConceptImpl(AnotherConcept):

    def __init__(self, actual_self: 'Optional[AnotherConcept]', etc: 'Etc', flag: 'bool' = True) -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[AnotherConcept]
        else:
            self.__actual_self = actual_self

        self.__etc = etc
        self.__flag = flag

    @property
    def etc(self) -> 'Etc':
        return self.__etc

    @property
    def flag(self) -> 'bool':
        return self.__flag

    def __str__(self) -> 'str':
        return "%s(etc=%s, flag=%s)" % (
            type(self).__name__,
            self.__actual_self.etc,
            self.__actual_self.flag
        )


class Bar(ABC):
    pass


class BarImpl(Bar):

    def __init__(self, actual_self: 'Optional[Bar]') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Bar]
        else:
            self.__actual_self = actual_self



    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class Etc(ABC):
    pass


class EtcImpl(Etc):

    def __init__(self, actual_self: 'Optional[Etc]') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[Etc]
        else:
            self.__actual_self = actual_self



    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__


class SomeConcept(ABC):

    @abstractproperty
    def value(self) -> 'int':
        pass

    @abstractproperty
    def bar(self) -> 'Bar':
        pass

    @abstractproperty
    def foos(self) -> 'List[AnotherConcept]':
        pass

    @abstractproperty
    def one_more_path(self) -> 'AnotherConcept':
        pass

    @staticmethod
    def create_some_concept(value: 'int', bar: 'Bar', foos: 'List[AnotherConcept]', one_more_path: 'AnotherConcept') -> 'SomeConcept':
        return SomeConceptImpl(None, value, bar, foos, one_more_path)

    @staticmethod
    def extend_some_concept(actual_self: 'Optional[SomeConcept]', value: 'int', bar: 'Bar', foos: 'List[AnotherConcept]', one_more_path: 'AnotherConcept') -> 'SomeConcept':
        return SomeConceptImpl(actual_self, value, bar, foos, one_more_path)


class SomeConceptImpl(SomeConcept):

    def __init__(self, actual_self: 'Optional[SomeConcept]', value: 'int', bar: 'Bar', foos: 'List[AnotherConcept]', one_more_path: 'AnotherConcept') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[SomeConcept]
        else:
            self.__actual_self = actual_self

        self.__value = value
        self.__bar = bar
        self.__foos = foos
        self.__one_more_path = one_more_path

    @property
    def value(self) -> 'int':
        return self.__value

    @property
    def bar(self) -> 'Bar':
        return self.__bar

    @property
    def foos(self) -> 'List[AnotherConcept]':
        return self.__foos

    @property
    def one_more_path(self) -> 'AnotherConcept':
        return self.__one_more_path

    def __str__(self) -> 'str':
        return "%s(value=%s, bar=%s, one_more_path=%s)" % (
            type(self).__name__,
            self.__actual_self.value,
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
    def path_var(self) -> 'Bar':
        pass

    @abstractproperty
    def path_var_2(self) -> 'Etc':
        pass

    @abstractproperty
    def path_var_3(self) -> 'List[Etc]':
        pass

    @abstractproperty
    def path_bars(self) -> 'List[Bar]':
        pass

    @abstractproperty
    def path_foos(self) -> 'List[AnotherConcept]':
        pass

    @abstractproperty
    def sorted_list(self) -> 'List[SomeConcept]':
        pass

    @abstractproperty
    def opt_prop(self) -> 'Optional[AnotherConcept]':
        pass

    @abstractproperty
    def opt_flag(self) -> 'bool':
        pass

    @abstractproperty
    def none_prop(self) -> 'Optional[SomeConcept]':
        pass

    @staticmethod
    def create_expression_cases(foo: 'str', some_path: 'SomeConcept', some_path_list: 'List[SomeConcept]', opt_prop: 'Optional[AnotherConcept]') -> 'ExpressionCases':
        return ExpressionCasesImpl(None, foo, some_path, some_path_list, opt_prop)

    @staticmethod
    def extend_expression_cases(actual_self: 'Optional[ExpressionCases]', foo: 'str', some_path: 'SomeConcept', some_path_list: 'List[SomeConcept]', opt_prop: 'Optional[AnotherConcept]') -> 'ExpressionCases':
        return ExpressionCasesImpl(actual_self, foo, some_path, some_path_list, opt_prop)


class ExpressionCasesImpl(ExpressionCases):

    def __init__(self, actual_self: 'Optional[ExpressionCases]', foo: 'str', some_path: 'SomeConcept', some_path_list: 'List[SomeConcept]', opt_prop: 'Optional[AnotherConcept]') -> 'None':
        if actual_self is None:
            self.__actual_self = self  # type: Optional[ExpressionCases]
        else:
            self.__actual_self = actual_self

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
        return self.__actual_self

    @property
    def single_var(self) -> 'str':
        return self.__actual_self.foo

    @property
    def derived_some_path(self) -> 'SomeConcept':
        return self.__actual_self.some_path

    @property
    def path_var(self) -> 'Bar':
        return self.__actual_self.some_path.bar

    @property
    def path_var_2(self) -> 'Etc':
        return self.__actual_self.some_path.one_more_path.etc

    @property
    def path_var_3(self) -> 'List[Etc]':
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
    def path_bars(self) -> 'List[Bar]':
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

    @property
    def sorted_list(self) -> 'List[SomeConcept]':
        return list(
            sorted(self.__actual_self.some_path_list, key=functools.cmp_to_key(lambda item_1, item_2: -1 if (item_1.value < item_2.value) else +1 if (item_2.value < item_1.value) else 0))
        )

    @property
    def opt_flag(self) -> 'bool':
        return any(map(lambda item: item.flag, [] if self.__actual_self.opt_prop is None else [self.__actual_self.opt_prop]))

    @property
    def none_prop(self) -> 'Optional[SomeConcept]':
        return None

    def __str__(self) -> 'str':
        return "%s(foo=%s, some_path=%s, single_var=%s, opt_prop=%s, opt_flag=%s)" % (
            type(self).__name__,
            self.__actual_self.foo,
            self.__actual_self.some_path,
            self.__actual_self.single_var,
            self.__actual_self.opt_prop,
            self.__actual_self.opt_flag
        )