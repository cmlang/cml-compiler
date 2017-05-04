grammar Expressions;

import Literals, Paths;

expression returns [Expression expr]
    : literalExpression
    | pathExpression
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
    | IF cond=expression THEN then=expression ELSE else_=expression
    | queryExpression;

queryExpression returns [Expression expr]
    : pathExpression
    | FOR enumeratorDeclaration (',' enumeratorDeclaration)*
    | queryExpression '|' transformDeclaration;

transformDeclaration returns [Transform transform]:
    (FROM var=NAME '=' init=expression)?
    operation=
        ( SELECT
        | REJECT
        | YIELD
        | RECURSE
        | TAKE
        | DROP
        | INCLUDES
        | EXCLUDES
        | EVERY
        | EXISTS
        | REDUCE
        | COUNT)
    suffix=(FIRST | UNIQUE | WHILE)?
    expr=expression?;

enumeratorDeclaration:
    NAME IN pathExpression;

