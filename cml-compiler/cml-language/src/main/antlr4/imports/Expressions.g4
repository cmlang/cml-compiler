grammar Expressions;

import Literals;

expression returns [Expression expr]
    : expression operator=('*' | '/') expression
    | expression operator=('+' | '-') expression
    | <assoc=right> expression operator='^' expression
    | literalExpression;


