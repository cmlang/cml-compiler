package shapes.console;

import static shapes.Circle.createCircle;
import static shapes.Rectangle.createRectangle;
import static shapes.RedUnitCircle.createRedUnitCircle;
import static shapes.Rhombus.createRhombus;
import static shapes.Square.createSquare;
import static shapes.UnitCircle.createUnitCircle;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Shapes Console\n");
        System.out.println(createRectangle(10, 20, "red"));
        System.out.println(createRhombus(14.1421356237, 14.1421356237, "green"));
        System.out.println(createSquare(10, "blue"));
        System.out.println(createCircle(10, "red"));
        System.out.println(createUnitCircle());
        System.out.println(createRedUnitCircle());
    }
}
