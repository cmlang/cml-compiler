package cml.primitives;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Type
{
    public static final List<String> NUMERIC_TYPE_NAMES = unmodifiableList(asList(
        "BYTE", "SHORT", "INTEGER", "LONG", "DECIMAL" // from narrower to wider
    ));

    public static final List<String> BINARY_FLOATING_POINT_TYPE_NAMES = unmodifiableList(asList(
        "FLOAT", "DOUBLE" // from narrower to wider
    ));

    public static boolean isNumeric(String typeName)
    {
        return NUMERIC_TYPE_NAMES.contains(typeName.toUpperCase());
    }

    public static boolean isBinaryFloatingPoint(String typeName)
    {
        return BINARY_FLOATING_POINT_TYPE_NAMES.contains(typeName.toUpperCase());
    }

    // Used by template: invocation_subtype(args)
    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean subtype(String s, String t)
    {
        if (isNumeric(s) && isNumeric(t))
        {
            return isNumericWiderThan(s, t);
        }
        else if (isBinaryFloatingPoint(s) && isBinaryFloatingPoint(t))
        {
            return isBinaryFloatingPointWiderThan(s, t);
        }
        else
        {
            return false;
        }
    }

    public static boolean isNumericWiderThan(String s, String t)
    {
        if (isNumeric(s) && isNumeric(t))
        {
            final int i = NUMERIC_TYPE_NAMES.indexOf(s.toUpperCase());
            final int j = NUMERIC_TYPE_NAMES.indexOf(t.toUpperCase());

            return i > j;
        }

        return false;
    }

    public static boolean isBinaryFloatingPointWiderThan(String s, String t)
    {
        if (isBinaryFloatingPoint(s) && isBinaryFloatingPoint(t))
        {
            final int i = BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(s.toUpperCase());
            final int j = BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(t.toUpperCase());

            return i > j;
        }

        return false;
    }
}
