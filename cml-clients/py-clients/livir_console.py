
from livir_books import Book
from decimal import *

book = Book("ISBN-1234", "Python's Book", "Python's Book", Decimal("13.00"))

print("Book: %s" % book)
