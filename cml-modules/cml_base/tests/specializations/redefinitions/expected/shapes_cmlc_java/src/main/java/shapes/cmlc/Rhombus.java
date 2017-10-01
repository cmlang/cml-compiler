package shapes.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface Rhombus extends Shape
{
    double getP();

    double getQ();

    double getArea();

    static Rhombus createRhombus(String color, double p, double q)
    {
        return new RhombusImpl(null, color, p, q);
    }

    static Rhombus extendRhombus(@Nullable Rhombus actual_self, Shape shape, double p, double q)
    {
        return new RhombusImpl(actual_self, shape, p, q);
    }
}

class RhombusImpl implements Rhombus
{
    private final @Nullable Rhombus actual_self;

    private final Shape shape;

    private final double p;
    private final double q;

    RhombusImpl(@Nullable Rhombus actual_self, String color, double p, double q)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = Shape.extendShape(this.actual_self, color);
        this.p = p;
        this.q = q;
    }

    RhombusImpl(@Nullable Rhombus actual_self, Shape shape, double p, double q)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = shape;
        this.p = p;
        this.q = q;
    }

    public double getP()
    {
        return this.p;
    }

    public double getQ()
    {
        return this.q;
    }

    public double getArea()
    {
        return ((this.actual_self.getP() * this.actual_self.getQ()) / 2.0);
    }

    public String getColor()
    {
        return this.shape.getColor();
    }

    public double getTotalArea()
    {
        return this.shape.getTotalArea();
    }

    public String toString()
    {
        return new StringBuilder(Rhombus.class.getSimpleName())
                   .append('(')
                   .append("p=").append(String.format("\"%s\"", this.actual_self.getP())).append(", ")
                   .append("q=").append(String.format("\"%s\"", this.actual_self.getQ())).append(", ")
                   .append("area=").append(String.format("\"%s\"", this.actual_self.getArea())).append(", ")
                   .append("color=").append(String.format("\"%s\"", this.actual_self.getColor())).append(", ")
                   .append("totalArea=").append(String.format("\"%s\"", this.actual_self.getTotalArea()))
                   .append(')')
                   .toString();
    }
}