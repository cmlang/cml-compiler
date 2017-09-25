from typing import *
from abc import *
from decimal import *

import itertools

class Product:

    def __init__(self, name: 'str', price: 'Decimal' = Decimal("0.00")) -> 'None':
        self.__name = name
        self.__price = price

    @property
    def name(self) -> 'str':
        return self.__name

    @property
    def price(self) -> 'Decimal':
        return self.__price

    def __str__(self) -> 'str':
        return "%s(name=%s, price=%s)" % (
            type(self).__name__,
            self.name,
            self.price
        )


class Book(Product):

    def __init__(self, price: 'Decimal' = Decimal("0.00"), isbn: 'str' = "Not Provided", title: 'str' = "No Title", name: 'str' = "") -> 'None':
        super().__init__(name, price)
        self.__isbn = isbn
        self.__title = title
        self.__name = name

    @property
    def isbn(self) -> 'str':
        return self.__isbn

    @property
    def title(self) -> 'str':
        return self.__title

    @property
    def name(self) -> 'str':
        return self.__name

    def __str__(self) -> 'str':
        return "%s(isbn=%s, title=%s, name=%s, price=%s)" % (
            type(self).__name__,
            self.isbn,
            self.title,
            self.name,
            self.price
        )


class Item:

    def __init__(self, book: 'Book', quantity: 'int') -> 'None':
        self.__book = book
        self.__quantity = quantity

    @property
    def book(self) -> 'Book':
        return self.__book

    @property
    def quantity(self) -> 'int':
        return self.__quantity

    def __str__(self) -> 'str':
        return "%s(book=%s, quantity=%s)" % (
            type(self).__name__,
            self.book,
            self.quantity
        )


class Customer:

    def __init__(self, name: 'str') -> 'None':
        self.__name = name

    @property
    def name(self) -> 'str':
        return self.__name

    def __str__(self) -> 'str':
        return "%s(name=%s)" % (
            type(self).__name__,
            self.name
        )


class Order:

    def __init__(self, customer: 'Customer', items: 'List[Item]') -> 'None':
        self.__customer = customer
        self.__items = items

    @property
    def customer(self) -> 'Customer':
        return self.__customer

    @property
    def items(self) -> 'List[Item]':
        return self.__items

    def __str__(self) -> 'str':
        return "%s(customer=%s)" % (
            type(self).__name__,
            self.customer
        )


class BookStore:

    def __init__(self, books: 'List[Book]', customers: 'List[Customer]') -> 'None':
        self.__books = books
        self.__customers = customers

    @property
    def books(self) -> 'List[Book]':
        return self.__books

    @property
    def customers(self) -> 'List[Customer]':
        return self.__customers

    def __str__(self) -> 'str':
        return "%s()" % type(self).__name__