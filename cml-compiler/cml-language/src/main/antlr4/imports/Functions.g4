grammar Functions;

import Names, Types, Annotations, Expressions;

functionDeclaration returns [Function function]:
  annotationList?
  FUNCTION name=NAME
  typeParams=typeParameterList?
  params=functionParameterList
  ('->' resultType=typeDeclaration)? ('=' expression)? ';';

functionParameterList returns [Seq<FunctionParameter> params]:
  '(' functionParameterDeclaration? (',' functionParameterDeclaration)* ')';

functionParameterDeclaration returns [FunctionParameter param]:
 name=NAME ':' type=typeDeclaration;
