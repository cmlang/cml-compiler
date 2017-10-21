lexer grammar Names;

// import: none

// All reserved words must be declared before NAME.
// Otherwise, they are recognized as a NAME instead.

FOR: 'for';
IN: 'in';
AS: 'as';
ASB: 'as!';
ASQ: 'as?';
IS: 'is';
ISNT: 'isnt';
IF: 'if';
THEN: 'then';
ELSE: 'else';
GIVEN: 'given';
UNLESS: 'unless';
ORQ: 'or?';
XORQ: 'xor?';
BOOLEAN: 'true' | 'false';
AND: 'and';
OR: 'or';
XOR: 'xor';
IMPLIES: 'implies';
NOT: 'not';
TEMPLATE: '@template';
FUNCTION: '@function';
ABSTRACTION: '@abstraction';
CONCEPT: '@concept';
TASK: '@task';
ASSOCIATION: '@association';
MODULE: '@module';
IMPORT: '@import';

NAME:
  ('A'..'Z' | 'a'..'z')
  ( 'A'..'Z' | 'a'..'z' | '0'..'9' | '_' )*;

