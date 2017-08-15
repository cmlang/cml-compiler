grammar Expressions;

import Literals;

expression returns [Expression expr]
    : literalExpression
    | pathExpression
    | conditionalExpression
    | assignmentExpression
    | invocationExpression
    | comprehensionExpression
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
    | '(' inner=expression ')';

invocationExpression returns [Invocation invocation]:
    NAME '(' expression (',' expression)* ')';

comprehensionExpression returns [Comprehension comprehension]:
    (pathExpression | FOR enumeratorDeclaration (',' enumeratorDeclaration)*) queryStatement+;

enumeratorDeclaration returns [Enumerator enumerator]:
    var=NAME IN pathExpression;

queryStatement returns [Query query]:
    '|' keywordExpression+;

keywordExpression returns [Keyword keyword]:
    NAME ':' expression;

conditionalExpression returns [Conditional conditional]:
    IF cond=expression
    THEN then=expression
    ELSE else_=expression;

assignmentExpression returns [Assignment assignment]:
    variable=NAME '=' value=expression;

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

