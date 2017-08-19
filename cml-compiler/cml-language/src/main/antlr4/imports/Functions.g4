grammar Functions;

import Names, Types;

functionDeclaration returns [Function function]:
    FUNCTION NAME functionParameterList '->' typeDeclaration ';';

functionParameterList:
    '(' functionParameterDeclaration? (',' functionParameterDeclaration)* ')';

functionParameterDeclaration returns [FunctionParameter parameter]:
   name=NAME ':' typeDeclaration;
