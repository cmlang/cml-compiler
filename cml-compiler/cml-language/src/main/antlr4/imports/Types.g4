grammar Types;

import Names;

typeDeclaration returns [NamedType type]
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




