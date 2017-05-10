grammar Literals;

import Names;

literalExpression returns [Literal literal]: BOOLEAN | STRING | INTEGER | DECIMAL;

STRING:
    '"' (ESC | . )*? '"';

fragment ESC: '\\'[btnr"\\];
    
INTEGER:
    ('0'..'9')+;

DECIMAL:
    ('0'..'9')* '.' ('0'..'9')+;
