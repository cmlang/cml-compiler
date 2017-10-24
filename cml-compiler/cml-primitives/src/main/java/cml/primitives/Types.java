package cml.primitives;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Types
{
    public static final String BOOLEAN = "boolean";
    public static final String STRING = "string";
    public static final String INTEGER = "integer";
    public static final String LONG = "long";
    public static final String SHORT = "short";
    public static final String BYTE =  "byte";
    public static final String DECIMAL = "decimal";
    public static final String FLOAT = "float";
    public static final String DOUBLE = "double";

    public static final Collection<String> PRIMITIVE_TYPE_NAMES = unmodifiableCollection(asList(
        BOOLEAN, INTEGER, DECIMAL, STRING, // "REGEX", // main primitive types
        BYTE, SHORT, LONG, FLOAT, DOUBLE // "CHAR" // remaining primitive types
    ));

    public static final List<String> NUMERIC_TYPE_NAMES = unmodifiableList(asList(
        BYTE, SHORT, INTEGER, LONG, DECIMAL // from narrower to wider
    ));

    public static final List<String> BINARY_FLOATING_POINT_TYPE_NAMES = unmodifiableList(asList(
        FLOAT, DOUBLE // from narrower to wider
    ));

    public static final List<String> ARITHMETIC_OPERATORS = unmodifiableList(asList(
        "+", "-", "*", "/", "%", "^"
    ));

    private static final Collection<String> LOGICAL_OPERATORS = unmodifiableCollection(asList(
        "and", "or", "xor", "implies"
    ));

    private static final Collection<String> RELATIONAL_OPERATORS = unmodifiableCollection(asList(
        "==", "!=", ">", ">=", "<", "<="
    ));

    public static String primitiveTypeName(final String name)
    {
        return name.toLowerCase();
    }

    public static boolean primitive(String typeName)
    {
        return PRIMITIVE_TYPE_NAMES.contains(primitiveTypeName(typeName));
    }

    public static boolean numeric(String typeName)
    {
        return NUMERIC_TYPE_NAMES.contains(primitiveTypeName(typeName));
    }

    public static boolean float_(String typeName)
    {
        return BINARY_FLOATING_POINT_TYPE_NAMES.contains(primitiveTypeName(typeName));
    }

    public static boolean boolean_(String typeName)
    {
        return primitiveTypeName(typeName).equals(BOOLEAN);
    }

    public static boolean string(String typeName)
    {
        return primitiveTypeName(typeName).equals(STRING);
    }

    // Used by template: invocation_subtype(args)
    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean subtype(String s, String t)
    {
        if (numeric(s) && numeric(t))
        {
            return numericSubType(s, t);
        }
        else if (float_(s) && float_(t))
        {
            return binaryFloatingPointSubtype(s, t);
        }
        else if (primitive(s) && primitive(t))
        {
            return primitiveTypeName(s).equals(primitiveTypeName(t));
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
            final int i = NUMERIC_TYPE_NAMES.indexOf(primitiveTypeName(s));
            final int j = NUMERIC_TYPE_NAMES.indexOf(primitiveTypeName(t));

            return i <= j;
        }

        return false;
    }

    public static boolean binaryFloatingPointSubtype(String s, String t)
    {
        if (float_(s) && float_(t))
        {
            final int i = BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(primitiveTypeName(s));
            final int j = BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(primitiveTypeName(t));

            return i <= j;
        }

        return false;
    }

    public static boolean arithmeticOperator(String operator)
    {
        return ARITHMETIC_OPERATORS.contains(operator);
    }

    public static boolean logicalOperator(String operator)
    {
        return LOGICAL_OPERATORS.contains(operator);
    }

    public static boolean relationalOperator(String operator)
    {
        return RELATIONAL_OPERATORS.contains(operator);
    }
}
