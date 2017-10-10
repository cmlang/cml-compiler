package cml.language.functions;

import cml.language.expressions.TypeCast;
import cml.language.features.FunctionParameter;
import cml.language.features.TempConcept;
import cml.language.generated.Type;
import cml.language.types.*;

import java.util.List;
import java.util.Objects;

import static cml.language.functions.ModelElementFunctions.selfTypeOf;
import static cml.language.types.NamedType.BINARY_FLOATING_POINT_TYPE_NAMES;
import static cml.language.types.NamedType.NUMERIC_TYPE_NAMES;
import static org.jooq.lambda.Seq.seq;
import static org.jooq.lambda.Seq.zip;

@SuppressWarnings("WeakerAccess")
public class TypeFunctions
{
    public static TempType withCardinality(Type type, String cardinality)
    {
        if (type instanceof NamedType)
        {
            final NamedType namedType = (NamedType) type;
            final NamedType newType = NamedType.create(namedType.getName(), cardinality);

            namedType.getConcept().map(c -> (TempConcept)c).ifPresent(newType::setConcept);

            return newType;
        }
        else if (type instanceof TupleType)
        {
            final TupleType tupleType = (TupleType) type;
            final TupleType newType = new TupleType(seq(tupleType.getElements()), cardinality);

            tupleType.getConcept().map(c -> (TempConcept)c).ifPresent(newType::setConcept);

            return newType;
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

                if (isNameEquals(thisNamedType, thatNamedType))
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
                    return seq(thatNamedType.getConcept()).map(c -> (TempConcept)c)
                                                          .flatMap(c -> c.getAllGeneralizations().stream())
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

            return isNameEquals(thisNamedType, thatNamedType) && isCardinalityEquals(thisNamedType, thatNamedType);
        }

        return false;
    }

    public static boolean isNameEquals(final NamedType thisType, final NamedType thatType)
    {
        assert thisType.getName() != null;
        assert thatType.getName() != null;

        if (thisType.isPrimitive() && thatType.isPrimitive())
        {
            return thisType.getName().equalsIgnoreCase(thatType.getName());
        }
        else
        {
            return thisType.getName().equals(thatType.getName());
        }
    }

    public static boolean isCardinalityEquals(final NamedType thisType, final NamedType thatType)
    {
        return Objects.equals(thisType.getCardinality(), thatType.getCardinality());
    }

    public static boolean isNumericWiderThan(Type thisType, Type thatType)
    {
        if (thisType instanceof NamedType && thatType instanceof NamedType)
        {
            final NamedType thisNamedType = (NamedType) thisType;
            final NamedType thatNamedType = (NamedType) thatType;

            assert thisNamedType.isNumeric() && thatNamedType.isNumeric()
                : "Both types must be numeric in order to be compared: " + thisNamedType.getName() + " & " + thatNamedType.getName();

            final int i = NUMERIC_TYPE_NAMES.indexOf(thisNamedType.getName().toUpperCase());
            final int j = NUMERIC_TYPE_NAMES.indexOf(thatNamedType.getName().toUpperCase());

            return i > j;
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

            final int i = BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(thisNamedType.getName().toUpperCase());
            final int j = BINARY_FLOATING_POINT_TYPE_NAMES.indexOf(thatNamedType.getName().toUpperCase());

            return i > j;
        }

        return false;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean isCastAllowed(String operator, Type exprType, Type castType)
    {
        if (operator.equals(TypeCast.ASQ) && castType.isRequired())
        {
            return false;
        }
        else
        {
            return isElementTypeAssignableFrom(exprType.getElementType(), castType.getElementType()) &&
                   isCardinalityAssignableFrom(castType, exprType);
        }
    }

    public static int getParamIndexOfMatchingType(List<FunctionParameter> parameters, TempType type)
    {
        return getParamIndexOfMatchingType(parameters, type, -1);
    }

    public static int getParamIndexOfMatchingType(List<FunctionParameter> parameters, TempType type, int skipIndex)
    {
        assert type.isParameter(): "Must be called only when type is a parameter.";

        int index = 0;
        for (FunctionParameter parameter: parameters)
        {
            if (isElementTypeAssignableFrom(parameter.getMatchingResultType().getElementType(), type.getBaseType().getElementType()))
            {
                if (index != skipIndex) break;
            }
            index++;
        }

        return index;
    }
}
