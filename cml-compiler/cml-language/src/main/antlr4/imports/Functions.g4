grammar Functions;

import Names, Types, Annotations;

functionDeclaration returns [Function function]:
  annotationList?
  FUNCTION name=NAME
  typeParams=typeParameterList?
  params=functionParameterList
  '->' resultType=typeDeclaration ';';

functionParameterList returns [Seq<FunctionParameter> params]:
  '(' functionParameterDeclaration? (',' functionParameterDeclaration)* ')';

functionParameterDeclaration returns [FunctionParameter param]:
 name=NAME ':' type=typeDeclaration;
