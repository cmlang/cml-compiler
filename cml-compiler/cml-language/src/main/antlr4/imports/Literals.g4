grammar Literals;

// import: none

literalExpression returns [Literal literal]: STRING | INTEGER | DECIMAL;

STRING:
    '"' .*? '"';

INTEGER:
    ('0'..'9')+;

DECIMAL:
    ('0'..'9')* '.' ('0'..'9')+;
