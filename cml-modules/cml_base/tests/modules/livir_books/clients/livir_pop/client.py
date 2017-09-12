
from livir_books import Book
from decimal import *

print("Livir Client (pop)\n")

book = Book(Decimal("13.00"), "ISBN-1234", "Python's Book", "Python's Book")

print("Book: %s" % book)
