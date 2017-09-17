package cml.language.functions;

import cml.language.types.*;

import java.util.Objects;

import static cml.language.functions.ModelElementFunctions.selfTypeOf;
import static cml.language.types.NamedType.BINARY_FLOATING_POINT_TYPE_NAMES;
import static cml.language.types.NamedType.NUMERIC_TYPE_NAMES;
import static org.jooq.lambda.Seq.seq;
import static org.jooq.lambda.Seq.zip;

@SuppressWarnings("WeakerAccess")
public class TypeFunctions
{
    public static Type withCardinality(Type type, String cardinality)
    {
        if (type instanceof NamedType)
        {
            final NamedType namedType = (NamedType) type;
            final NamedType newType = NamedType.create(namedType.getName(), cardinality);

            namedType.getConcept().ifPresent(newType::setConcept);

            return namedType;
        }
        else if (type instanceof TupleType)
        {
            final TupleType tupleType = (TupleType) type;
            final TupleType newType = new TupleType(seq(tupleType.getElements()), cardinality);

            tupleType.getConcept().ifPresent(newType::setConcept);

            return tupleType;
        }
        else
        {
            throw new UnsupportedOperationException("withCardinality(" + type + ", " + cardinality + ")");
        }
    }

    public static boolean isElementTypeAssignableFrom(Type thisElementType, Type thatElementType)
    {
        assert !thisElementType.getCardinality().isPresent();
        assert !thatElementType.getCardinality().isPresent();

        if (thisElementType instanceof NamedType)
        {
            final NamedType thisNamedType = (NamedType) thisElementType;

            if (thatElementType instanceof NamedType)
            {
                final NamedType thatNamedType = (NamedType) thatElementType;

                if (thisNamedType.getName().equals(thatNamedType.getName()))
                {
                    return true;
                }
                else if (thisNamedType.isNumeric() && thatNamedType.isNumeric())
                {
                    return isNumericWiderThan(thisNamedType, thatNamedType);
                }
                else if (thisNamedType.isBinaryFloatingPoint() && thatNamedType.isBinaryFloatingPoint())
                {
                    return isBinaryFloatingPointWiderThan(thisNamedType, thatNamedType);
                }
                else if (thisNamedType.getConcept().isPresent() && thatNamedType.getConcept().isPresent())
                {
                    return seq(thatNamedType.getConcept()).flatMap(c -> c.getAllGeneralizations().stream())
                                                          .anyMatch(c -> isElementTypeAssignableFrom(thisNamedType, selfTypeOf(c)));
                }
            }

            return false;
        }
        else if (thisElementType instanceof TupleType)
        {
            final TupleType thisTupleType = (TupleType) thisElementType;

            if (thatElementType instanceof TupleType)
            {
                final TupleType thatTupleType = (TupleType)thatElementType;

                return zip(thisTupleType.getElements(), thatTupleType.getElements()).allMatch(t -> t.v1.isAssignableFrom(t.v2));
            }

            return false;
        }
        else if (thisElementType instanceof FunctionType)
        {
            final FunctionType thisFunctionType = (FunctionType) thisElementType;

            if (thatElementType instanceof FunctionType)
            {
                final FunctionType thatFunctionType = (FunctionType)thatElementType;

                return isAssignableFrom(thisFunctionType.getParams(), thatFunctionType.getParams()) &&
                       isAssignableFrom(thisFunctionType.getResult(), thatFunctionType.getResult());
            }

            return false;
        }
        else if (thisElementType instanceof MemberType)
        {
            final MemberType thisMemberType = (MemberType) thisElementType;

            return isElementTypeAssignableFrom(thisMemberType.getBaseType(), thatElementType);
        }
        else
        {
            throw new UnsupportedOperationException("isElementTypeAssignableFrom(" + thisElementType + ", " + thatElementType + ")");
        }
    }

    public static boolean isCardinalityAssignableFrom(Type thisType, Type thatType)
    {
        return Objects.equals(thisType.getCardinality(), thatType.getCardinality()) ||
               (thisType.isOptional() && thatType.isRequired()) || (thisType.isSequence());
    }

    public static boolean isAssignableFrom(Type thisType, Type thatType)
    {
        return isElementTypeAssignableFrom(thisType.getElementType(), thatType.getElementType()) &&
               isCardinalityAssignableFrom(thisType, thatType);
    }

    public static boolean isEqualTo(Type thisType, Type thatType)
    {
        if (thisType instanceof NamedType && thatType instanceof NamedType)
        {
            final NamedType thisNamedType = (NamedType) thisType;
            final NamedType thatNamedType = (NamedType) thatType;

            return Objects.equals(thisNamedType.getName(), thatNamedType.getName()) &&
                   Objects.equals(thisNamedType.getCardinality(), thatNamedType.getCardinality());
        }

        return false;
    }

    public static boolean isNumericWiderThan(Type thisType, Type thatType)
    {
        if (thisType instanceof NamedType && thatType instanceof NamedType)
        {
            final NamedType thisNamedType = (NamedType) thisType;
            final NamedType thatNamedType = (NamedType) thatType;

            assert thisNamedType.isNumeric() && thatNamedType.isNumeric()
                : "Both types must be numeric in order to be compared: " + thisNamedType.getName() + " & " + thatNamedType.getName();

            return NUMERIC_TYPE_NAMES.indexOf(thisNamedType.getName()) > NUMERIC_TYPE_NAMES.indexOf(thatNamedType.getName());
        }

        return false;
    }

    public static boolean isBinaryFloatingPointWiderThan(Type thisType, Type thatType)
    {
        if (thisType instanceof NamedType && thatType instanceof NamedType)
        {
            final NamedType thisNamedType = (NamedType) thisType;
            final NamedType thatNamedType = (NamedType) thatType;

            assert thisNamedType.isBinaryFloatingPoint() && thatNamedType.isBinaryFloatingPoint()
                : "Both types must be binary floating-point in order to be compared: " + thisNamedType.getName() + " & " + thatNamedType.getName();

            return BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(thisNamedType.getName()) > BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(thatNamedType.getName());
        }

        return false;
    }
}
