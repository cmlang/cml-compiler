package shapes.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface Circle extends Shape
{
    double getRadius();

    double getArea();

    String getColor();

    static Circle createCircle(double radius)
    {
        return createCircle(radius, "Blue");
    }

    static Circle createCircle(double radius, String color)
    {
        return new CircleImpl(null, radius, color);
    }

    static Circle extendCircle(@Nullable Circle actual_self, Shape shape, double radius, String color)
    {
        return new CircleImpl(actual_self, shape, radius, color);
    }
}

class CircleImpl implements Circle
{
    private final @Nullable Circle actual_self;

    private final Shape shape;

    private final double radius;
    private final String color;

    CircleImpl(@Nullable Circle actual_self, double radius, String color)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = Shape.extendShape(this.actual_self, color);
        this.radius = radius;
        this.color = color;
    }

    CircleImpl(@Nullable Circle actual_self, Shape shape, double radius, String color)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = shape;
        this.radius = radius;
        this.color = color;
    }

    public double getRadius()
    {
        return this.radius;
    }

    public String getColor()
    {
        return this.color;
    }

    public double getArea()
    {
        return (3.14159 * (Math.pow(getRadius(), 2.0)));
    }

    public String toString()
    {
        return new StringBuilder(Circle.class.getSimpleName())
                   .append('(')
                   .append("radius=").append(String.format("\"%s\"", getRadius())).append(", ")
                   .append("area=").append(String.format("\"%s\"", getArea())).append(", ")
                   .append("color=").append(String.format("\"%s\"", getColor()))
                   .append(')')
                   .toString();
    }
}