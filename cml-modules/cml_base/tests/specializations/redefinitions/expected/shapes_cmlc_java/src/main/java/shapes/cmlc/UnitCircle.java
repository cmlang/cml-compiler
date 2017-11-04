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
        return new UnitCircleImpl(null, color, area);
    }

    static UnitCircle extendUnitCircle(@Nullable UnitCircle actual_self, Shape shape, Circle circle, double area)
    {
        return new UnitCircleImpl(actual_self, shape, circle, area);
    }
}

class UnitCircleImpl implements UnitCircle
{
    private final @Nullable UnitCircle actual_self;

    private final Shape shape;
    private final Circle circle;

    private final double area;

    UnitCircleImpl(@Nullable UnitCircle actual_self, String color, double area)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = Shape.extendShape(this.actual_self, color);
        this.circle = Circle.extendCircle(this.actual_self, this.shape, 0.0f, color);
        this.area = area;
    }

    UnitCircleImpl(@Nullable UnitCircle actual_self, Shape shape, Circle circle, double area)
    {
        this.actual_self = actual_self == null ? this : actual_self;

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

    public double getTotalArea()
    {
        return this.shape.getTotalArea();
    }

    public String toString()
    {
        return new StringBuilder(UnitCircle.class.getSimpleName())
                   .append('(')
                   .append("area=").append(String.format("\"%s\"", this.actual_self.getArea())).append(", ")
                   .append("radius=").append(String.format("\"%s\"", this.actual_self.getRadius())).append(", ")
                   .append("color=").append(String.format("\"%s\"", this.actual_self.getColor())).append(", ")
                   .append("totalArea=").append(String.format("\"%s\"", this.actual_self.getTotalArea()))
                   .append(')')
                   .toString();
    }
}