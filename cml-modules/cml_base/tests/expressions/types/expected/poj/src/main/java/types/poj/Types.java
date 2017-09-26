package types.poj;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public class Types
{
    private final Ancestor singleAncestor;
    private final List<Ancestor> ancestors;

    public Types(Ancestor singleAncestor, List<Ancestor> ancestors)
    {
        this.singleAncestor = singleAncestor;
        this.ancestors = ancestors;
    }

    public Ancestor getSingleAncestor()
    {
        return this.singleAncestor;
    }

    public List<Ancestor> getAncestors()
    {
        return Collections.unmodifiableList(this.ancestors);
    }

    public boolean isTypeCheckIs()
    {
        return getSingleAncestor() instanceof Descendant;
    }

    public boolean isTypeCheckIsNot()
    {
        return !(getSingleAncestor() instanceof Descendant);
    }

    public Descendant getTypeCast()
    {
        return (Descendant)getSingleAncestor();
    }

    public List<Ancestor> getDescendants()
    {
        return seq(getAncestors()).filter(a -> a instanceof Descendant).toList();
    }

    public String toString()
    {
        return new StringBuilder(Types.class.getSimpleName())
                   .append('(')
                   .append("singleAncestor=").append(String.format("\"%s\"", getSingleAncestor())).append(", ")
                   .append("typeCheckIs=").append(String.format("\"%s\"", isTypeCheckIs())).append(", ")
                   .append("typeCheckIsNot=").append(String.format("\"%s\"", isTypeCheckIsNot()))
                   .append(')')
                   .toString();
    }
}