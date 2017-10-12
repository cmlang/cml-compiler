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
        return this.getReq() instanceof Descendant;
    }

    public boolean isTypeCheckIsNot()
    {
        return !(this.getReq() instanceof Descendant);
    }

    public Descendant getReqToReqTypeCastAsb()
    {
        return (Descendant)this.getReq();
    }

    public Descendant getOptToReqTypeCastAsb()
    {
        return seq(this.getOpt()).cast(Descendant.class).findFirst().get();
    }

    public Optional<Descendant> getReqToOptTypeCastAsb()
    {
        return seq(asList(this.getReq())).cast(Descendant.class).findFirst();
    }

    public Optional<Descendant> getReqToOptTypeCastAsq()
    {
        return seq(asList(this.getReq())).ofType(Descendant.class).findFirst();
    }

    public Optional<Descendant> getOptToOptTypeCastAsb()
    {
        return seq(this.getOpt()).cast(Descendant.class).findFirst();
    }

    public Optional<Descendant> getOptToOptTypeCastAsq()
    {
        return seq(this.getOpt()).ofType(Descendant.class).findFirst();
    }

    public List<Descendant> getReqToSeqTypeCastAsb()
    {
        return seq(asList(this.getReq())).cast(Descendant.class).toList();
    }

    public List<Descendant> getOptToSeqTypeCastAsb()
    {
        return seq(this.getOpt()).cast(Descendant.class).toList();
    }

    public List<Descendant> getSeqToSeqTypeCastAsb()
    {
        return seq(this.getSeq()).cast(Descendant.class).toList();
    }

    public List<Descendant> getReqToSeqTypeCastAsq()
    {
        return seq(asList(this.getReq())).ofType(Descendant.class).toList();
    }

    public List<Descendant> getOptToSeqTypeCastAsq()
    {
        return seq(this.getOpt()).ofType(Descendant.class).toList();
    }

    public List<Descendant> getSeqToSeqTypeCastAsq()
    {
        return seq(this.getSeq()).ofType(Descendant.class).toList();
    }

    public List<Ancestor> getDescendants()
    {
        return seq(this.getSeq()).filter((a) -> a instanceof Descendant).toList();
    }

    public String toString()
    {
        return new StringBuilder(Types.class.getSimpleName())
                   .append('(')
                   .append("req=").append(String.format("\"%s\"", this.getReq())).append(", ")
                   .append("opt=").append(this.getOpt().isPresent() ? String.format("\"%s\"", this.getOpt()) : "not present").append(", ")
                   .append("typeCheckIs=").append(String.format("\"%s\"", this.isTypeCheckIs())).append(", ")
                   .append("typeCheckIsNot=").append(String.format("\"%s\"", this.isTypeCheckIsNot()))
                   .append(')')
                   .toString();
    }
}