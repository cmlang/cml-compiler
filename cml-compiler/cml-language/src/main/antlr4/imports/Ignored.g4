lexer grammar Ignored;

// import: none

// Ignoring Whitespace:

WS:
    ( ' ' | '\t' | '\f' | '\n' | '\r' )+ -> skip;


// Ignoring Comments:

COMMENT:
    ('//' .*? '\n' | '(*' .*? '*)' ) -> skip;

    
