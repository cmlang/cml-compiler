package paths.poj;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Bar
{
    public String toString()
    {
        return new StringBuilder(Bar.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }
}