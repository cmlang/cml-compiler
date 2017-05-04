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
    | IF cond=expression THEN then=expression ELSE else_=expression;

