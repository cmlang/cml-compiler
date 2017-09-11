package shapes.client;

import static shapes.cmlc.Circle.createCircle;
import static shapes.cmlc.Rectangle.createRectangle;
import static shapes.cmlc.RedUnitCircle.createRedUnitCircle;
import static shapes.cmlc.Rhombus.createRhombus;
import static shapes.cmlc.Square.createSquare;
import static shapes.cmlc.UnitCircle.createUnitCircle;

public class Launcher
{
    public static void main(final String[] args)
    {
        System.out.println("Shapes Client (cmlc_java)\n");
        System.out.println(createRectangle("red", 10, 20));
        System.out.println(createRhombus("green", 14.1421356237, 14.1421356237));
        System.out.println(createSquare("blue", 10));
        System.out.println(createCircle(10, "red"));
        System.out.println(createUnitCircle());
        System.out.println(createRedUnitCircle());
    }
}
