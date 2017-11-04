package types.poj;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Ancestor
{
    public String toString()
    {
        return new StringBuilder(Ancestor.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }
}