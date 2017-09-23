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
        return new RhombusImpl(color, p, q);
    }

    static Rhombus extendRhombus(Shape shape, double p, double q)
    {
        return new RhombusImpl(shape, p, q);
    }
}

class RhombusImpl implements Rhombus
{
    private final Shape shape;

    private final double p;
    private final double q;

    RhombusImpl(String color, double p, double q)
    {
        this.shape = Shape.extendShape(color);
        this.p = p;
        this.q = q;
    }

    RhombusImpl(Shape shape, double p, double q)
    {
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
        return ((getP() * getQ()) / 2.0);
    }

    public String getColor()
    {
        return this.shape.getColor();
    }

    public String toString()
    {
        return new StringBuilder(Rhombus.class.getSimpleName())
                   .append('(')
                   .append("p=").append(String.format("\"%s\"", getP())).append(", ")
                   .append("q=").append(String.format("\"%s\"", getQ())).append(", ")
                   .append("area=").append(String.format("\"%s\"", getArea())).append(", ")
                   .append("color=").append(String.format("\"%s\"", getColor()))
                   .append(')')
                   .toString();
    }
}