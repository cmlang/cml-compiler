grammar Expressions;

import Literals, Paths;

expression returns [Expression expr]
    : literalExpression
    | pathExpression
    | operator=('+' | '-' | NOT) expression
    | <assoc=right> expression operator='^' expression
    | expression operator=('*' | '/' | '%') expression
    | expression operator=('+' | '-') expression
    | expression operator=('==' | '!=' | '<' | '<=' | '>' | '>=') expression
    | expression operator=AND expression
    | expression operator=OR expression
    | IF cond=expression THEN then=expression ELSE else_=expression
    | collectionExpression;

collectionExpression:
    (pathExpression | collectionComprehension) ('|' collectionTransformation)+;

collectionComprehension:
    FOR collectionEnumerator (',' collectionEnumerator)*;

collectionEnumerator:
    NAME IN pathExpression;

collectionTransformation:
    (FROM var=NAME '=' init=expression)?
    operation=
        ( SELECT
        | REJECT
        | YIELD
        | FLATTEN
        | RECURSE
        | TAKE
        | DROP
        | INCLUDES
        | EXCLUDES
        | EVERY
        | EXISTS
        | REDUCE
        | COUNT)
    suffix=(FIRST | UNIQUE)
    expression?;
