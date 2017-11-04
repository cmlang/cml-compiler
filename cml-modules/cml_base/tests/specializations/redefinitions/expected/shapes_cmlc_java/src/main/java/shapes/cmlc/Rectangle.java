package shapes.cmlc;

import java.util.*;
import java.util.function.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface Rectangle extends Shape
{
    double getWidth();

    double getHeight();

    double getArea();

    static Rectangle createRectangle(String color, double width, double height)
    {
        return new RectangleImpl(null, color, width, height);
    }

    static Rectangle extendRectangle(@Nullable Rectangle actual_self, Shape shape, double width, double height)
    {
        return new RectangleImpl(actual_self, shape, width, height);
    }
}

class RectangleImpl implements Rectangle
{
    private final @Nullable Rectangle actual_self;

    private final Shape shape;

    private final double width;
    private final double height;

    RectangleImpl(@Nullable Rectangle actual_self, String color, double width, double height)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = Shape.extendShape(this.actual_self, color);
        this.width = width;
        this.height = height;
    }

    RectangleImpl(@Nullable Rectangle actual_self, Shape shape, double width, double height)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = shape;
        this.width = width;
        this.height = height;
    }

    public double getWidth()
    {
        return this.width;
    }

    public double getHeight()
    {
        return this.height;
    }

    public double getArea()
    {
        return (this.actual_self.getWidth() * this.actual_self.getHeight());
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
        return new StringBuilder(Rectangle.class.getSimpleName())
                   .append('(')
                   .append("width=").append(String.format("\"%s\"", this.actual_self.getWidth())).append(", ")
                   .append("height=").append(String.format("\"%s\"", this.actual_self.getHeight())).append(", ")
                   .append("area=").append(String.format("\"%s\"", this.actual_self.getArea())).append(", ")
                   .append("color=").append(String.format("\"%s\"", this.actual_self.getColor())).append(", ")
                   .append("totalArea=").append(String.format("\"%s\"", this.actual_self.getTotalArea()))
                   .append(')')
                   .toString();
    }
}