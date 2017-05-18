grammar Literals;

import Names;

literalExpression returns [Literal literal]: BOOLEAN | STRING | INTEGER | LONG | SHORT | BYTE | DECIMAL | FLOAT | DOUBLE;

STRING:
    '"' (ESC | . )*? '"';

fragment ESC: '\\'[btnr"\\];
    
INTEGER:
    ('0'..'9')+;

LONG:
    ('0'..'9')+ 'l';

SHORT:
    ('0'..'9')+ 's';

BYTE:
    ('0'..'9')+ 'b';

DECIMAL:
    ('0'..'9')* '.' ('0'..'9')+;

FLOAT:
    ('0'..'9')* '.' ('0'..'9')+ 'f';

DOUBLE:
    ('0'..'9')* '.' ('0'..'9')+ 'd';
