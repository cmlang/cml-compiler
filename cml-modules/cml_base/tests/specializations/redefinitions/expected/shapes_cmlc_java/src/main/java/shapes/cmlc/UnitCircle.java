package shapes.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface UnitCircle extends Circle
{
    double getArea();

    double getRadius();

    static UnitCircle createUnitCircle()
    {
        return createUnitCircle("Blue", 3.14159);
    }

    static UnitCircle createUnitCircle(String color, double area)
    {
        return new UnitCircleImpl(color, area);
    }

    static UnitCircle extendUnitCircle(Shape shape, Circle circle, double area)
    {
        return new UnitCircleImpl(shape, circle, area);
    }
}

class UnitCircleImpl implements UnitCircle
{
    private final Shape shape;
    private final Circle circle;

    private final double area;

    UnitCircleImpl(String color, double area)
    {
        this.shape = Shape.extendShape(color);
        this.circle = Circle.extendCircle(this.shape, 0.0f, color);
        this.area = area;
    }

    UnitCircleImpl(Shape shape, Circle circle, double area)
    {
        this.shape = shape;
        this.circle = circle;
        this.area = area;
    }

    public double getArea()
    {
        return this.area;
    }

    public double getRadius()
    {
        return 1.0;
    }

    public String getColor()
    {
        return this.circle.getColor();
    }

    public String toString()
    {
        return new StringBuilder(UnitCircle.class.getSimpleName())
                   .append('(')
                   .append("area=").append(String.format("\"%s\"", getArea())).append(", ")
                   .append("radius=").append(String.format("\"%s\"", getRadius())).append(", ")
                   .append("color=").append(String.format("\"%s\"", getColor()))
                   .append(')')
                   .toString();
    }
}