grammar Functions;

import Names, Types;

functionDeclaration returns [Function function]:
    FUNCTION name=NAME
    typeParams=typeParameterList?
    params=functionParameterList
    '->' resultType=typeDeclaration ';';

functionParameterList returns [Stream<FunctionParameter> params]:
    '(' functionParameterDeclaration? (',' functionParameterDeclaration)* ')';

functionParameterDeclaration returns [FunctionParameter param]:
   name=NAME ':' type=typeDeclaration;
