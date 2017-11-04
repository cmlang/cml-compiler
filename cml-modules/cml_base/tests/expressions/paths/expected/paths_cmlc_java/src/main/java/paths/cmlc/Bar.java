package paths.cmlc;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface Bar {}

class BarImpl implements Bar
{
    private final @Nullable Bar actual_self;

    BarImpl(@Nullable Bar actual_self)
    {
        this.actual_self = actual_self == null ? this : actual_self;


    }

    public String toString()
    {
        return new StringBuilder(Bar.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }
}