grammar Expressions;

import Literals;

expression returns [Expression expr]
    : literalExpression
    | operator=('+' | '-') expression
    | <assoc=right> expression operator='^' expression
    | expression operator=('*' | '/' | '%') expression
    | expression operator=('+' | '-') expression
    | expression operator=('==' | '!=' | '<' | '<=' | '>' | '>=') expression;


