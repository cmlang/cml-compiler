package shapes.cmlc;

import java.util.*;
import java.math.*;
import org.jetbrains.annotations.*;
import org.jooq.lambda.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.jooq.lambda.Seq.*;

public interface RedUnitCircle extends UnitCircle
{
    String getColor();

    static RedUnitCircle createRedUnitCircle()
    {
        return createRedUnitCircle(3.14159, "Red");
    }

    static RedUnitCircle createRedUnitCircle(double area, String color)
    {
        return new RedUnitCircleImpl(null, area, color);
    }

    static RedUnitCircle extendRedUnitCircle(@Nullable RedUnitCircle actual_self, Shape shape, Circle circle, UnitCircle unitCircle, String color)
    {
        return new RedUnitCircleImpl(actual_self, shape, circle, unitCircle, color);
    }
}

class RedUnitCircleImpl implements RedUnitCircle
{
    private final @Nullable RedUnitCircle actual_self;

    private final Shape shape;
    private final Circle circle;
    private final UnitCircle unitCircle;

    private final String color;

    RedUnitCircleImpl(@Nullable RedUnitCircle actual_self, double area, String color)
    {
        this.actual_self = actual_self == null ? this : actual_self;

        this.shape = Shape.extendShape(this.actual_self, color);
        this.circle = Circle.extendCircle(this.actual_self, this.shape, 0.0f, color);
        this.unitCircle = UnitCircle.extendUnitCircle(this.actual_self, this.shape, this.circle, area);
        this.color = color;
    }

    RedUnitCircleImpl(@Nullable RedUnitCircle actual_self, Shape shape, Circle circle, UnitCircle unitCircle, String color)
    {
        this.actual_self = actual_self == null ? this : actual_self;

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

    public double getTotalArea()
    {
        return this.shape.getTotalArea();
    }

    public String toString()
    {
        return new StringBuilder(RedUnitCircle.class.getSimpleName())
                   .append('(')
                   .append("color=").append(String.format("\"%s\"", this.actual_self.getColor())).append(", ")
                   .append("area=").append(String.format("\"%s\"", this.actual_self.getArea())).append(", ")
                   .append("radius=").append(String.format("\"%s\"", this.actual_self.getRadius())).append(", ")
                   .append("totalArea=").append(String.format("\"%s\"", this.actual_self.getTotalArea()))
                   .append(')')
                   .toString();
    }
}