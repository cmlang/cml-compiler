package shapes.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface RedUnitCircle extends UnitCircle
{
    String getColor();

    static RedUnitCircle createRedUnitCircle()
    {
        return createRedUnitCircle(3.14159, "Red");
    }

    static RedUnitCircle createRedUnitCircle(double area, String color)
    {
        return new RedUnitCircleImpl(area, color);
    }

    static RedUnitCircle extendRedUnitCircle(Shape shape, Circle circle, UnitCircle unitCircle, String color)
    {
        return new RedUnitCircleImpl(shape, circle, unitCircle, color);
    }
}

class RedUnitCircleImpl implements RedUnitCircle
{
    private final Shape shape;
    private final Circle circle;
    private final UnitCircle unitCircle;

    private final String color;

    RedUnitCircleImpl(double area, String color)
    {
        this.shape = Shape.extendShape(color);
        this.circle = Circle.extendCircle(this.shape, 0.0f, color);
        this.unitCircle = UnitCircle.extendUnitCircle(this.shape, this.circle, area);
        this.color = color;
    }

    RedUnitCircleImpl(Shape shape, Circle circle, UnitCircle unitCircle, String color)
    {
        this.shape = shape;
        this.circle = circle;
        this.unitCircle = unitCircle;
        this.color = color;
    }

    public String getColor()
    {
        return this.color;
    }

    public double getArea()
    {
        return this.unitCircle.getArea();
    }

    public double getRadius()
    {
        return this.unitCircle.getRadius();
    }

    public String toString()
    {
        return new StringBuilder(RedUnitCircle.class.getSimpleName())
                   .append('(')
                   .append("color=").append(String.format("\"%s\"", getColor())).append(", ")
                   .append("area=").append(String.format("\"%s\"", getArea())).append(", ")
                   .append("radius=").append(String.format("\"%s\"", getRadius()))
                   .append(')')
                   .toString();
    }
}