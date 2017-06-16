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
    | IF cond=expression
      THEN then=expression
      ELSE else_=expression
    | variable=NAME assignment=':=' value=expression
    | queryExpression
    | invocationExpression
    | input=expression pipe='|' macro=NAME lambda=expression
    | '(' inner=expression ')';

queryExpression returns [Expression expr]
    : pathExpression
    | joinExpression
    | queryExpression '|' transformDeclaration;

joinExpression returns [Join join]:
    FOR enumeratorDeclaration (',' enumeratorDeclaration)*;

enumeratorDeclaration:
    var=NAME IN pathExpression;

transformDeclaration returns [Transform transform]:
    (FROM var=NAME '=' init=expression)?
    operation=
        ( SELECT   | REJECT
        | YIELD    | RECURSE
        | INCLUDES | EXCLUDES
        | EVERY    | EXISTS
        | REDUCE
        | TAKE     | DROP
        | FIRST    | LAST
        | COUNT    | SUM       | AVERAGE
        | MAX      | MIN
        | REVERSE)
    suffix=(UNIQUE | WHILE)?
    expr=expression?;

invocationExpression returns [Invocation invocation]:
    NAME '(' expression (',' expression)* ')';
