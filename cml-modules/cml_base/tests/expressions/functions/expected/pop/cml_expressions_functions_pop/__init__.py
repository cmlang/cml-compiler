from typing import *
from abc import *
from decimal import *

class Item:

    def __init__(self, size: 'int') -> 'None':
        self.__size = size

    @property
    def size(self) -> 'int':
        return self.__size

    def __str__(self) -> 'str':
        return "%s(size=%s)" % (
            type(self).__name__,
            self.size
        )


class Functions:

    def __init__(self, required_item: 'Item', single_item: 'Optional[Item]', items: 'List[Item]') -> 'None':
        self.__required_item = required_item
        self.__single_item = single_item
        self.__items = items

    @property
    def required_item(self) -> 'Item':
        return self.__required_item

    @property
    def single_item(self) -> 'Optional[Item]':
        return self.__single_item

    @property
    def items(self) -> 'List[Item]':
        return self.__items

    @property
    def empty_items(self) -> 'bool':
        return (len(self.items) == 0)

    @property
    def present_items(self) -> 'bool':
        return (len(self.items) > 0)

    @property
    def empty_single_item(self) -> 'bool':
        return (self.single_item is None)

    @property
    def present_single_item(self) -> 'bool':
        return (self.single_item is not None)

    @property
    def required_empty_single_item(self) -> 'bool':
        return (self.required_item is None)

    @property
    def required_present_single_item(self) -> 'bool':
        return (self.required_item is not None)

    @property
    def items_first(self) -> 'Optional[Item]':
        return ((self.items) or [None])[0]

    @property
    def items_last(self) -> 'Optional[Item]':
        return ((self.items) or [None])[-1]

    @property
    def single_item_first(self) -> 'Optional[Item]':
        return self.single_item

    @property
    def single_item_last(self) -> 'Optional[Item]':
        return self.single_item

    @property
    def required_item_first(self) -> 'Optional[Item]':
        return self.required_item

    @property
    def required_item_last(self) -> 'Optional[Item]':
        return self.required_item

    @property
    def at_least_one_large_item(self) -> 'bool':
        return any(map(lambda item: (item.size > 100), self.items))

    @property
    def all_large_items(self) -> 'bool':
        return all(map(lambda item: (item.size > 100), self.items))

    @property
    def large_item_exists(self) -> 'bool':
        return any(map(lambda item: (item.size > 100), [] if self.single_item is None else [self.single_item]))

    @property
    def large_item_all(self) -> 'bool':
        return all(map(lambda item: (item.size > 100), [] if self.single_item is None else [self.single_item]))

    @property
    def required_item_exists(self) -> 'bool':
        return any(map(lambda item: (item.size > 100), [self.required_item]))

    @property
    def required_item_all(self) -> 'bool':
        return all(map(lambda item: (item.size > 100), [self.required_item]))

    def __str__(self) -> 'str':
        return "%s(required_item=%s, single_item=%s, empty_items=%s, present_items=%s, empty_single_item=%s, present_single_item=%s, required_empty_single_item=%s, required_present_single_item=%s, at_least_one_large_item=%s, all_large_items=%s, large_item_exists=%s, large_item_all=%s, required_item_exists=%s, required_item_all=%s)" % (
            type(self).__name__,
            self.required_item,
            self.single_item,
            self.empty_items,
            self.present_items,
            self.empty_single_item,
            self.present_single_item,
            self.required_empty_single_item,
            self.required_present_single_item,
            self.at_least_one_large_item,
            self.all_large_items,
            self.large_item_exists,
            self.large_item_all,
            self.required_item_exists,
            self.required_item_all
        )