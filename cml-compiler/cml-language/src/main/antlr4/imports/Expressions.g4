grammar Expressions;

import Names, Literals;

expression returns [Expression expr]
    : literalExpression
    | pathExpression
    | operator=('+' | '-') expression
    | <assoc=right> expression operator='^' expression
    | expression operator=('*' | '/' | '%') expression
    | expression operator=('+' | '-') expression
    | expression operator=('==' | '!=' | '<' | '<=' | '>' | '>=') expression
    | expression operator=AND expression
    | expression operator=OR expression;

pathExpression returns [Path path]:
    NAME ('.' NAME)*;
