grammar Literals;

import Names;

literalExpression returns [Literal literal]: BOOLEAN | STRING | INTEGER | DECIMAL;

STRING:
    '"' .*? '"';

INTEGER:
    ('0'..'9')+;

DECIMAL:
    ('0'..'9')* '.' ('0'..'9')+;
