package cml.language.functions;

import cml.language.expressions.TypeCast;
import cml.language.features.FunctionParameter;
import cml.language.features.TempConcept;
import cml.language.generated.Type;
import cml.language.generated.ValueType;
import cml.language.types.FunctionType;
import cml.language.types.MemberType;
import cml.language.types.TempNamedType;
import cml.language.types.TupleType;

import java.util.List;
import java.util.Objects;

import static cml.language.functions.ModelElementFunctions.selfTypeOf;
import static cml.language.generated.ValueType.createValueType;
import static cml.primitives.Types.subtype;
import static org.jooq.lambda.Seq.seq;
import static org.jooq.lambda.Seq.zip;

@SuppressWarnings("WeakerAccess")
public class TypeFunctions
{
    public static Type withCardinality(Type type, String cardinality)
    {
        if (type instanceof ValueType)
        {
            return createValueType(cardinality, type.getName());
        }
        else if (type instanceof TempNamedType)
        {
            final TempNamedType namedType = (TempNamedType) type;
            final TempNamedType newType = TempNamedType.create(namedType.getName(), cardinality);

            namedType.getConcept().map(c -> (TempConcept)c).ifPresent(newType::setConcept);

            return newType;
        }
        else if (type instanceof TupleType)
        {
            final TupleType tupleType = (TupleType) type;
            return new TupleType(seq(tupleType.getElements()), cardinality);
        }
        else if (type.isUndefined())
        {
            return type;
        }
        else
        {
            throw new UnsupportedOperationException("withCardinality(" + type + ", " + cardinality + ")");
        }
    }

    public static boolean isElementTypeAssignableFrom(Type leftType, Type rightType)
    {
        assert leftType.isRequired();
        assert rightType.isRequired();

        if (leftType instanceof ValueType && rightType instanceof ValueType)
        {
            return subtype(rightType.getName(), leftType.getName());
        }
        else if (leftType instanceof TempNamedType)
        {
            final TempNamedType leftNamedType = (TempNamedType) leftType;

            if (rightType instanceof TempNamedType)
            {
                final TempNamedType rightNamedType = (TempNamedType) rightType;

                if (isNameEquals(leftNamedType, rightNamedType))
                {
                    return true;
                }
                else if (leftNamedType.getConcept().isPresent() && rightNamedType.getConcept().isPresent())
                {
                    return seq(rightNamedType.getConcept()).map(c -> (TempConcept)c)
                                                          .flatMap(c -> c.getAllGeneralizations().stream())
                                                          .anyMatch(c -> isElementTypeAssignableFrom(leftNamedType, selfTypeOf(c)));
                }
            }

            return false;
        }
        else if (leftType instanceof TupleType)
        {
            final TupleType leftTupleType = (TupleType) leftType;

            if (rightType instanceof TupleType)
            {
                final TupleType rightTupleType = (TupleType)rightType;

                return zip(leftTupleType.getElements(), rightTupleType.getElements()).allMatch(t -> t.v1.isAssignableFrom(t.v2));
            }

            return false;
        }
        else if (leftType instanceof FunctionType)
        {
            final FunctionType leftFunctionType = (FunctionType) leftType;

            if (rightType instanceof FunctionType)
            {
                final FunctionType rightFunctionType = (FunctionType)rightType;

                return isAssignableFrom(leftFunctionType.getParams(), rightFunctionType.getParams()) &&
                       isAssignableFrom(leftFunctionType.getResult(), rightFunctionType.getResult());
            }

            return false;
        }
        else if (leftType instanceof MemberType)
        {
            final MemberType leftMemberType = (MemberType) leftType;

            return isElementTypeAssignableFrom(leftMemberType.getBaseType(), rightType);
        }
        else
        {
            return false;
        }
    }

    public static boolean isCardinalityAssignableFrom(Type thisType, Type thatType)
    {
        return Objects.equals(thisType.getCardinality(), thatType.getCardinality()) ||
               (thisType.isOptional() && thatType.isRequired()) || (thisType.isSequence());
    }

    public static boolean isAssignableFrom(Type thisType, Type thatType)
    {
        if (thatType.isNothing())
        {
            return thisType.isSomething() && !thisType.isRequired();
        }
        else
        {
            return isElementTypeAssignableFrom(thisType.getElementType(), thatType.getElementType()) &&
                isCardinalityAssignableFrom(thisType, thatType);
        }
    }

    public static boolean isEqualTo(Type thisType, Type thatType)
    {
        if (thisType instanceof ValueType && thatType instanceof ValueType)
        {
            return isNameEquals(thisType, thatType) && isCardinalityEquals(thisType, thatType);
        }
        else if (thisType instanceof TempNamedType && thatType instanceof TempNamedType)
        {
            return isNameEquals(thisType, thatType) && isCardinalityEquals(thisType, thatType);
        }

        return false;
    }

    public static boolean isNameEquals(final Type thisType, final Type thatType)
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

    public static boolean isCardinalityEquals(final Type thisType, final Type thatType)
    {
        return Objects.equals(thisType.getCardinality(), thatType.getCardinality());
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean isCastAllowed(String operator, Type exprType, Type castType)
    {
        if (operator.equals(TypeCast.ASQ) && castType.isRequired())
        {
            return false;
        }
        else if (operator.equals(TypeCast.ASB) && castType.isRequired())
        {
            return isElementTypeAssignableFrom(exprType.getElementType(), castType.getElementType()) && exprType.isSingle();
        }
        else
        {
            return (isElementGeneralizationAssignableFrom(exprType.getElementType(), castType.getElementType()) &&
                    isCardinalityAssignableFrom(castType, exprType));
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean isElementGeneralizationAssignableFrom(final Type thisElementType, final Type thatElementType)
    {
        if (isElementTypeAssignableFrom(thisElementType, thatElementType)) return true;

        return seq(thisElementType.getConcept()).map(c -> (TempConcept)c)
                                                .flatMap(c -> c.getAllGeneralizations().stream())
                                                .anyMatch(c -> isElementTypeAssignableFrom(selfTypeOf(c), thatElementType));
    }

    public static int getParamIndexOfMatchingType(List<FunctionParameter> parameters, Type type)
    {
        return getParamIndexOfMatchingType(parameters, type, -1);
    }

    public static int getParamIndexOfMatchingType(List<FunctionParameter> parameters, Type type, int skipIndex)
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
