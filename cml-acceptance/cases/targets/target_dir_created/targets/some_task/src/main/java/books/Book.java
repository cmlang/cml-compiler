package books;

import java.util.*;
import org.jetbrains.annotations.*;

public class Book
{
    public String toString()
    {
        return new StringBuilder(Book.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }
}