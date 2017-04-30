package books;

import java.util.*;
import org.jetbrains.annotations.*;

public class BookStore
{
    public String toString()
    {
        return new StringBuilder(BookStore.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }
}