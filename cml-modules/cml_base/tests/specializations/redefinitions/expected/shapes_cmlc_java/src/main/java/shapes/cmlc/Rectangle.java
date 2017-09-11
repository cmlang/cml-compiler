package shapes.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Rectangle extends Shape
{
    double getWidth();

    double getHeight();

    double getArea();

    static Rectangle createRectangle(String color, double width, double height)
    {
        return new RectangleImpl(color, width, height);
    }

    static Rectangle extendRectangle(Shape shape, double width, double height)
    {
        return new RectangleImpl(shape, width, height);
    }
}

class RectangleImpl implements Rectangle
{
    private final Shape shape;

    private final double width;
    private final double height;

    RectangleImpl(String color, double width, double height)
    {
        this.shape = Shape.extendShape(color);
        this.width = width;
        this.height = height;
    }

    RectangleImpl(Shape shape, double width, double height)
    {
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
        return (getWidth() * getHeight());
    }

    public String getColor()
    {
        return this.shape.getColor();
    }

    public String toString()
    {
        return new StringBuilder(Rectangle.class.getSimpleName())
                   .append('(')
                   .append("width=").append(String.format("\"%s\"", getWidth())).append(", ")
                   .append("height=").append(String.format("\"%s\"", getHeight())).append(", ")
                   .append("area=").append(String.format("\"%s\"", getArea())).append(", ")
                   .append("color=").append(String.format("\"%s\"", getColor()))
                   .append(')')
                   .toString();
    }
}