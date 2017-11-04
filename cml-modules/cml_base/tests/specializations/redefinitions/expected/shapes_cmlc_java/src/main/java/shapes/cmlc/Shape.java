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

public interface Shape
{
    String getColor();

    double getArea();

    double getTotalArea();

    static Shape extendShape(@Nullable Shape actual_self, String color)
    {
        return new ShapeImpl(actual_self, color);
    }
}

class ShapeImpl implements Shape
{
    private final @Nullable Shape actual_self;

    private final String color;

    ShapeImpl(@Nullable Shape actual_self, String color)
    {
        this.actual_self = actual_self == null ? this : actual_self;

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

    public double getTotalArea()
    {
        return this.actual_self.getArea();
    }

    public String toString()
    {
        return new StringBuilder(Shape.class.getSimpleName())
                   .append('(')
                   .append("color=").append(String.format("\"%s\"", this.actual_self.getColor())).append(", ")
                   .append("area=").append(String.format("\"%s\"", this.actual_self.getArea())).append(", ")
                   .append("totalArea=").append(String.format("\"%s\"", this.actual_self.getTotalArea()))
                   .append(')')
                   .toString();
    }
}