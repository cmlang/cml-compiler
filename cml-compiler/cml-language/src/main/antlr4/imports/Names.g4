lexer grammar Names;

// import: none

// All keywords must be declared before NAME.
// Otherwise, they are recognized as a NAME instead.

FOR: 'for';

IN: 'in';

SELECT: 'select';

REJECT: 'reject';

YIELD: 'yield';

FLATTEN: 'flatten';

RECURSE: 'recurse';

INCLUDES: 'includes';

EXCLUDES: 'excludes';

COUNT: 'count';

EVERY: 'every';

EXISTS: 'exists';

REDUCE: 'reduce';

TAKE: 'take';

DROP: 'drop';

FROM: 'from';

FIRST: 'first';

UNIQUE: 'unique';

IF: 'if';

THEN: 'then';

ELSE: 'else';

BOOLEAN: 'true' | 'false';

AND: 'and';

OR: 'or';

NOT: 'not';

ABSTRACT:
    'abstract';

NAME:
    ('A'..'Z' | 'a'..'z')
    ( 'A'..'Z' | 'a'..'z' | '0'..'9' | '_' )*;

