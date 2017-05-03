lexer grammar Names;

// import: none

// All keywords must be declared before NAME.
// Otherwise, they are recognized as a NAME instead.

BOOLEAN: 'true' | 'false';

AND: 'and';

OR: 'or';

ABSTRACT:
    'abstract';

NAME:
    ('A'..'Z' | 'a'..'z')
    ( 'A'..'Z' | 'a'..'z' | '0'..'9' | '_' )*;

