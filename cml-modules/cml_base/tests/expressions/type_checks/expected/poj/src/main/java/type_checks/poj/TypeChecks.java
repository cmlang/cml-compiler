package type_checks.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class TypeChecks
{
    private final List<Ancestor> ancestors;

    public TypeChecks(List<Ancestor> ancestors)
    {
        this.ancestors = ancestors;
    }

    public List<Ancestor> getAncestors()
    {
        return Collections.unmodifiableList(this.ancestors);
    }

    public boolean isTypeCheckIs()
    {
        return this instanceof TypeChecks;
    }

    public boolean isTypeCheckIsNot()
    {
        return !(this instanceof TypeChecks);
    }

    public List<Ancestor> getDescendants()
    {
        return seq(getAncestors()).filter(a -> a instanceof Descendant).toList();
    }

    public String toString()
    {
        return new StringBuilder(TypeChecks.class.getSimpleName())
                   .append('(')
                   .append("typeCheckIs=").append(String.format("\"%s\"", isTypeCheckIs())).append(", ")
                   .append("typeCheckIsNot=").append(String.format("\"%s\"", isTypeCheckIsNot()))
                   .append(')')
                   .toString();
    }
}