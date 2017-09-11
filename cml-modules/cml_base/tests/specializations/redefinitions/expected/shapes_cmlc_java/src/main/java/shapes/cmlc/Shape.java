package shapes.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

public interface Shape
{
    String getColor();

    double getArea();

    static Shape extendShape(String color)
    {
        return new ShapeImpl(color);
    }
}

class ShapeImpl implements Shape
{
    private final String color;

    ShapeImpl(String color)
    {
        this.color = color;
    }

    public String getColor()
    {
        return this.color;
    }

    public double getArea()
    {
        return 0.0f;
    }

    public String toString()
    {
        return new StringBuilder(Shape.class.getSimpleName())
                   .append('(')
                   .append("color=").append(String.format("\"%s\"", getColor())).append(", ")
                   .append("area=").append(String.format("\"%s\"", getArea()))
                   .append(')')
                   .toString();
    }
}