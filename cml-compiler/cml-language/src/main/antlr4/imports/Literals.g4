lexer grammar Literals;

// import: none

STRING:
    '"' .*? '"';

INTEGER:
    ('0'..'9')+;

DECIMAL:
    ('0'..'9')* '.' ('0'..'9')+;
