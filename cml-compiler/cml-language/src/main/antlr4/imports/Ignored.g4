lexer grammar Ignored;

// Ignoring whitespace:
WS:
    ( ' ' | '\t' | '\f' | '\n' | '\r' )+ -> skip;

// Ignoring comments:
COMMENT:
    ('//' .*? '\n' | '(*' .*? '*)' ) -> skip;
