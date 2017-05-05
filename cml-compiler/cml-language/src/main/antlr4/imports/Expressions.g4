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
        | INCLUDES
        | EXCLUDES
        | EVERY
        | EXISTS
        | REDUCE
        | TAKE
        | DROP
        | FIRST
        | LAST
        | COUNT
        | SUM
        | AVERAGE
        | MAX
        | MIN
        | REVERSE)
    suffix=(UNIQUE | WHILE)?
    expr=expression?;

enumeratorDeclaration:
    NAME IN pathExpression;

