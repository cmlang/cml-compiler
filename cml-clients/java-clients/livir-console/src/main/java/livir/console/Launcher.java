package livir.console;

import livir.books.Book;
import livir.books.BookStore;

import java.math.BigDecimal;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Livir Console");
        System.out.println();
        System.out.println("Classes:");
        System.out.println("- " + BookStore.class.getName());
        System.out.println("- " + Book.class.getName());
        System.out.println();
        System.out.println(new Book(new BigDecimal("10.00"), "1234", "Programming Adventures"));
    }
}
