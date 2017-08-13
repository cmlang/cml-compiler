grammar Expressions;

import Literals;

expression returns [Expression expr]
    : literalExpression
    | pathExpression
    | comprehensionExpression
    | invocationExpression
    | operator=('+' | '-' | NOT) expression
    | <assoc=right> expression operator='^' expression
    | expression operator=('*' | '/' | '%') expression
    | expression operator=('+' | '-') expression
    | expression operator=('<' | '<=' | '>' | '>=') expression
    | expression operator=('==' | '!=') expression
    | expression operator=AND expression
    | expression operator=OR expression
    | expression operator=XOR expression
    | expression operator=IMPLIES expression
    | IF cond=expression
      THEN then=expression
      ELSE else_=expression
    | variable=NAME assignment='=' value=expression
    | keyword=NAME ':' arg=expression
    | expression operator=',' expression
    | expression operator='|' expression
    | '(' inner=expression ')';

comprehensionExpression returns [Comprehension comprehension]:
    FOR enumeratorDeclaration (',' enumeratorDeclaration)* '|' expression;

enumeratorDeclaration returns [Enumerator enumerator]:
    var=NAME IN pathExpression;

invocationExpression returns [Invocation invocation]:
    NAME '(' expression (',' expression)* ')';

pathExpression returns [Path path]:
    NAME ('.' NAME)*;

//transformDeclaration returns [Transform transform]:
//    (FROM var=NAME '=' init=expression)?
//    operation=
//        ( REJECT
//        | YIELD    | RECURSE
//        | INCLUDES | EXCLUDES
//        | EVERY    | EXISTS
//        | REDUCE
//        | TAKE     | DROP
//        | FIRST    | LAST
//        | COUNT    | SUM       | AVERAGE
//        | MAX      | MIN
//        | REVERSE)
//    suffix=(UNIQUE | WHILE)?
//    expr=expression?;

