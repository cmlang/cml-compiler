package paths.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface Etc {}

class EtcImpl implements Etc
{
    private final @Nullable Etc actual_self;

    EtcImpl(@Nullable Etc actual_self)
    {
        this.actual_self = actual_self == null ? this : actual_self;


    }

    public String toString()
    {
        return new StringBuilder(Etc.class.getSimpleName())
                   .append('(')
                   .append(')')
                   .toString();
    }
}