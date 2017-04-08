package livir.console;

import livir.books.Book;
import livir.books.BookStore;

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
        System.out.println(new Book(10, "1234", "Programming Adventures"));
    }
}
