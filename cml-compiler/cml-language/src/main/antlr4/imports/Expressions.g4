grammar Expressions;

import Literals, Types;

expression returns [TempExpression expr]
    : literalExpression
    | pathExpression
    | conditionalExpression
    | lambdaExpression
    | invocationExpression
    | comprehensionExpression
    | operator=('+' | '-' | NOT) expression
    | <assoc=right> expression operator='^' expression
    | expression operator=('*' | '/' | '%') expression
    | expression operator=('+' | '-') expression
    | expression operator=('<' | '<=' | '>' | '>=') expression
    | expression operator=('===' | '!==' | '==' | '!=') expression
    | expression operator=(IS | ISNT) type=typeDeclaration
    | expression operator=(ASB | ASQ) type=typeDeclaration
    | expression operator=AND expression
    | expression operator=OR expression
    | expression operator=XOR expression
    | expression operator=IMPLIES expression
    | '(' inner=expression ')';

lambdaExpression returns[Lambda lambda]:
    '{' lambdaParameterList? expression '}';

lambdaParameterList returns[Seq<String> params]:
    NAME (',' NAME)* '->';

invocationExpression returns [Invocation invocation]:
    NAME '(' expression (',' expression)* ')' lambdaExpression?;

comprehensionExpression returns [Comprehension comprehension]:
    (pathExpression | FOR enumeratorDeclaration (',' enumeratorDeclaration)*) queryStatement+;

enumeratorDeclaration returns [Enumerator enumerator]:
    var=NAME IN pathExpression;

queryStatement returns [Query query]:
    '|' (NAME | keywordExpression+);

keywordExpression returns [Keyword keyword]:
    NAME ':' lambdaParameterList? expression;

conditionalExpression returns [Conditional conditional]:
    IF cond=expression
    THEN then=expression
    ELSE else_=expression;

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

