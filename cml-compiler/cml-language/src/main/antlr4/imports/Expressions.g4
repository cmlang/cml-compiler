grammar Expressions;

import Literals, Types;

expression returns [Expression expr]
  : literalExpression
  | pathExpression
  | lambdaExpression
  | invocationExpression
  | comprehensionExpression

  // Grouping
  | '(' inner=expression ')'

  // Arithmetic Expressions:
  | operator=('+' | '-' | NOT) expression
  | <assoc=right> expression operator='^' expression
  | expression operator=('*' | '/' | '%') expression
  | expression operator=('+' | '-') expression

  // String Concatenation:
  | expression operator='&' expression

  // Relational Expressions:
  | expression operator=('<' | '<=' | '>' | '>=') expression
  | expression operator=('==' | '!=') expression

  // Referential Expressions:
  | expression operator=('===' | '!==') expression

  // Type-Checking:
  | expression operator=(IS | ISNT) type=typeDeclaration

  // Type-Casting:
  | expression operator=(AS | ASB | ASQ) type=typeDeclaration

  // Logical Expressions:
  | expression operator=AND expression
  | expression operator=OR expression
  | expression operator=XOR expression
  | expression operator=IMPLIES expression

  // Conditional Expressions:
  | IF cond=expression THEN then=expression ELSE else_=expression
  | then=expression conj=(GIVEN | UNLESS) cond=expression
  | then=expression (ORQ | XORQ) else_=expression;


pathExpression returns [Path path]:
  NAME ('.' NAME)*;

lambdaExpression returns[Lambda lambda]:
  '{' lambdaParameterList? expression '}';

lambdaParameterList returns[Seq<String> params]:
  NAME (',' NAME)* '->';

invocationExpression returns [Invocation invocation]:
  NAME '(' expression (',' expression)* ')' lambdaExpression?;

comprehensionExpression returns [Comprehension comprehension]:
  (pathExpression |
   FOR enumeratorDeclaration
   (',' enumeratorDeclaration)*) queryStatement+;

enumeratorDeclaration returns [Enumerator enumerator]:
  var=NAME IN pathExpression;

queryStatement returns [Query query]:
  '|' (NAME | keywordExpression+);

keywordExpression returns [Keyword keyword]:
  NAME ':' lambdaParameterList? expression;


