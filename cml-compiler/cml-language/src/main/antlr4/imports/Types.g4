grammar Types;

import Names;

typeDeclaration returns [Type type]
    : name=NAME cardinality? // named type
    | tuple=tupleTypeDeclaration // tuple type
    | params=tupleTypeDeclaration '->' result=typeDeclaration // function type
    | '(' inner=typeDeclaration ')';

cardinality:
    ('?' | '*');

tupleTypeDeclaration:
    '(' tupleElementDeclaration? (',' tupleElementDeclaration)* ')';

tupleElementDeclaration:
   (name=NAME ':')? typeDeclaration;

typeParameterList returns [Stream<TypeParameter> params]:
    '<' typeParameter (',' typeParameter)* '>';

typeParameter returns [TypeParameter param]:
    name=NAME;




