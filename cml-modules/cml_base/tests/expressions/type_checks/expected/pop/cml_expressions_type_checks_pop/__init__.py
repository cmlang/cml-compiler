from typing import *
from abc import *
from decimal import *

import itertools

class TypeChecks:

    @property
    def type_check_is(self) -> 'bool':
        return isinstance(self, TypeChecks)

    @property
    def type_check_is_not(self) -> 'bool':
        return (not isinstance(self, TypeChecks))

    def __str__(self) -> 'str':
        return "%s(type_check_is=%s, type_check_is_not=%s)" % (
            type(self).__name__,
            self.type_check_is,
            self.type_check_is_not
        )