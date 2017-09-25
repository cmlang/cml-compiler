from typing import *
from abc import *
from decimal import *

import itertools

class LiteralExpressions:

    def __init__(self, literal_true_boolean: 'bool' = True, literal_false_boolean: 'bool' = False, literal_string_init: 'str' = "\tSome \"String\"\n", literal_integer_init: 'int' = 123, literal_decimal_init: 'Decimal' = Decimal("123.456"), literal_decimal_init_2: 'Decimal' = Decimal(".456")) -> 'None':
        self.__literal_true_boolean = literal_true_boolean
        self.__literal_false_boolean = literal_false_boolean
        self.__literal_string_init = literal_string_init
        self.__literal_integer_init = literal_integer_init
        self.__literal_decimal_init = literal_decimal_init
        self.__literal_decimal_init_2 = literal_decimal_init_2

    @property
    def literal_true_boolean(self) -> 'bool':
        return self.__literal_true_boolean

    @property
    def literal_false_boolean(self) -> 'bool':
        return self.__literal_false_boolean

    @property
    def literal_string_init(self) -> 'str':
        return self.__literal_string_init

    @property
    def literal_integer_init(self) -> 'int':
        return self.__literal_integer_init

    @property
    def literal_decimal_init(self) -> 'Decimal':
        return self.__literal_decimal_init

    @property
    def literal_decimal_init_2(self) -> 'Decimal':
        return self.__literal_decimal_init_2

    def __str__(self) -> 'str':
        return "%s(literal_true_boolean=%s, literal_false_boolean=%s, literal_string_init=%s, literal_integer_init=%s, literal_decimal_init=%s, literal_decimal_init_2=%s)" % (
            type(self).__name__,
            self.literal_true_boolean,
            self.literal_false_boolean,
            self.literal_string_init,
            self.literal_integer_init,
            self.literal_decimal_init,
            self.literal_decimal_init_2
        )