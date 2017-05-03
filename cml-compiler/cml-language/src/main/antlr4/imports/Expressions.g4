grammar Expressions;

import Literals;

expression returns [Expression expr]
    : literalExpression
    | <assoc=right> expression operator='^' expression
    | expression operator=('*' | '/') expression
    | expression operator=('+' | '-') expression
    | expression operator=('==' | '!=' | '<' | '<=' | '>' | '>=') expression;


