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
    private final Ancestor req;
    private final @Nullable Ancestor opt;
    private final List<Ancestor> seq;

    public Types(Ancestor req, @Nullable Ancestor opt, List<Ancestor> seq)
    {
        this.req = req;
        this.opt = opt;
        this.seq = seq;
    }

    public Ancestor getReq()
    {
        return this.req;
    }

    public Optional<Ancestor> getOpt()
    {
        return Optional.ofNullable(this.opt);
    }

    public List<Ancestor> getSeq()
    {
        return Collections.unmodifiableList(this.seq);
    }

    public boolean isTypeCheckIs()
    {
        return getReq() instanceof Descendant;
    }

    public boolean isTypeCheckIsNot()
    {
        return !(getReq() instanceof Descendant);
    }

    public Descendant getReqToReqTypeCastAsb()
    {
        return (Descendant)getReq();
    }

    public Optional<Descendant> getReqToOptTypeCastAsb()
    {
        return seq(asList(getReq())).cast(Descendant.class).findFirst();
    }

    public Optional<Descendant> getReqToOptTypeCastAsq()
    {
        return seq(asList(getReq())).ofType(Descendant.class).findFirst();
    }

    public Optional<Descendant> getOptToOptTypeCastAsb()
    {
        return seq(getOpt()).cast(Descendant.class).findFirst();
    }

    public Optional<Descendant> getOptToOptTypeCastAsq()
    {
        return seq(getOpt()).ofType(Descendant.class).findFirst();
    }

    public List<Descendant> getReqToSeqTypeCastAsb()
    {
        return seq(asList(getReq())).cast(Descendant.class).toList();
    }

    public List<Descendant> getOptToSeqTypeCastAsb()
    {
        return seq(getOpt()).cast(Descendant.class).toList();
    }

    public List<Descendant> getSeqToSeqTypeCastAsb()
    {
        return seq(getSeq()).cast(Descendant.class).toList();
    }

    public List<Descendant> getReqToSeqTypeCastAsq()
    {
        return seq(asList(getReq())).ofType(Descendant.class).toList();
    }

    public List<Descendant> getOptToSeqTypeCastAsq()
    {
        return seq(getOpt()).ofType(Descendant.class).toList();
    }

    public List<Descendant> getSeqToSeqTypeCastAsq()
    {
        return seq(getSeq()).ofType(Descendant.class).toList();
    }

    public List<Ancestor> getDescendants()
    {
        return seq(getSeq()).filter(a -> a instanceof Descendant).toList();
    }

    public String toString()
    {
        return new StringBuilder(Types.class.getSimpleName())
                   .append('(')
                   .append("req=").append(String.format("\"%s\"", getReq())).append(", ")
                   .append("opt=").append(getOpt().isPresent() ? String.format("\"%s\"", getOpt()) : "not present").append(", ")
                   .append("typeCheckIs=").append(String.format("\"%s\"", isTypeCheckIs())).append(", ")
                   .append("typeCheckIsNot=").append(String.format("\"%s\"", isTypeCheckIsNot()))
                   .append(')')
                   .toString();
    }
}