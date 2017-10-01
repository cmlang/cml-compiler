package shapes.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface Square extends Rectangle, Rhombus
{
    double getSideLength();

    double getWidth();

    double getHeight();

    double getP();

    double getQ();

    double getArea();

    static Square createSquare(String color, double sideLength)
    {
        return new SquareImpl(null, color, sideLength);
    }

    static Square extendSquare(@Nullable Square actual_self, Shape shape, Rectangle rectangle, Rhombus rhombus, double sideLength)
    {
        return new SquareImpl(actual_self, shape, rectangle, rhombus, sideLength);
    }
}

class SquareImpl implements Square
{
    private final @Nullable Square actual_self;

    private final Shape shape;
    private final Rectangle rectangle;
    private final Rhombus rhombus;

    private final double sideLength;

    SquareImpl(@Nullable Square actual_self, String color, double sideLength)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = Shape.extendShape(this.actual_self, color);
        this.rectangle = Rectangle.extendRectangle(this.actual_self, this.shape, 0.0f, 0.0f);
        this.rhombus = Rhombus.extendRhombus(this.actual_self, this.shape, 0.0f, 0.0f);
        this.sideLength = sideLength;
    }

    SquareImpl(@Nullable Square actual_self, Shape shape, Rectangle rectangle, Rhombus rhombus, double sideLength)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = shape;
        this.rectangle = rectangle;
        this.rhombus = rhombus;
        this.sideLength = sideLength;
    }

    public double getSideLength()
    {
        return this.sideLength;
    }

    public double getWidth()
    {
        return this.actual_self.getSideLength();
    }

    public double getHeight()
    {
        return this.actual_self.getSideLength();
    }

    public double getP()
    {
        return (this.actual_self.getSideLength() * 1.41421356237);
    }

    public double getQ()
    {
        return this.actual_self.getP();
    }

    public double getArea()
    {
        return (Math.pow(this.actual_self.getSideLength(), 2.0));
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
        return new StringBuilder(Square.class.getSimpleName())
                   .append('(')
                   .append("sideLength=").append(String.format("\"%s\"", this.actual_self.getSideLength())).append(", ")
                   .append("width=").append(String.format("\"%s\"", this.actual_self.getWidth())).append(", ")
                   .append("height=").append(String.format("\"%s\"", this.actual_self.getHeight())).append(", ")
                   .append("p=").append(String.format("\"%s\"", this.actual_self.getP())).append(", ")
                   .append("q=").append(String.format("\"%s\"", this.actual_self.getQ())).append(", ")
                   .append("area=").append(String.format("\"%s\"", this.actual_self.getArea())).append(", ")
                   .append("color=").append(String.format("\"%s\"", this.actual_self.getColor())).append(", ")
                   .append("totalArea=").append(String.format("\"%s\"", this.actual_self.getTotalArea()))
                   .append(')')
                   .toString();
    }
}