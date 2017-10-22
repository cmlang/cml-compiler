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

    public static final List<String> ARITHMETIC_OPERATORS = unmodifiableList(asList(
        "+", "-", "*", "/", "%", "^"
    ));

    public static boolean numeric(String typeName)
    {
        return NUMERIC_TYPE_NAMES.contains(typeName.toUpperCase());
    }

    public static boolean binaryFloatingPoint(String typeName)
    {
        return BINARY_FLOATING_POINT_TYPE_NAMES.contains(typeName.toUpperCase());
    }

    // Used by template: invocation_subtype(args)
    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean subtype(String s, String t)
    {
        if (numeric(s) && numeric(t))
        {
            return numericSubType(s, t);
        }
        else if (binaryFloatingPoint(s) && binaryFloatingPoint(t))
        {
            return binaryFloatingPointSubtype(s, t);
        }
        else
        {
            return false;
        }
    }

    public static boolean numericSubType(String s, String t)
    {
        if (numeric(s) && numeric(t))
        {
            final int i = NUMERIC_TYPE_NAMES.indexOf(s.toUpperCase());
            final int j = NUMERIC_TYPE_NAMES.indexOf(t.toUpperCase());

            return i <= j;
        }

        return false;
    }

    public static boolean binaryFloatingPointSubtype(String s, String t)
    {
        if (binaryFloatingPoint(s) && binaryFloatingPoint(t))
        {
            final int i = BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(s.toUpperCase());
            final int j = BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(t.toUpperCase());

            return i <= j;
        }

        return false;
    }

    public static boolean arithmeticOperator(String operator)
    {
        return ARITHMETIC_OPERATORS.contains(operator);
    }
}
