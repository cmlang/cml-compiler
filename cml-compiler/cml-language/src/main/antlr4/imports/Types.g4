grammar Types;

import Names;

typeDeclaration returns [Type type]
    : name=NAME cardinality? // named type
    | tuple=tupleTypeDeclaration // tuple type
    | params=tupleTypeDeclaration '->' result=typeDeclaration // function type
    | '(' inner=typeDeclaration ')';

cardinality:
    ('?' | '*');

tupleTypeDeclaration returns [TupleType type]:
    '(' ( tupleTypeElementDeclaration (',' tupleTypeElementDeclaration)* )? ')';

tupleTypeElementDeclaration returns [TupleTypeElement element]:
   (name=NAME ':')? type=typeDeclaration;

typeParameterList returns [Seq<TypeParameter> params]:
    '<' typeParameter (',' typeParameter)* '>';

typeParameter returns [TypeParameter param]:
    name=NAME;




