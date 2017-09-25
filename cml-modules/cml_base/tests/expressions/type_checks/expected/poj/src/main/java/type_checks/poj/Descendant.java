package type_checks.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Descendant extends Ancestor
{
    public String toString()
    {
        return new StringBuilder(Descendant.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }
}