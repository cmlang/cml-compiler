grammar CML;

@header
{
import cml.language.foundation.*;
import cml.language.features.*;
}

modelNode returns [Model model]:
    modelElementNode*;

modelElementNode:
    conceptNode | targetNode;

conceptNode returns [Concept concept]:
    ABSTRACT? 'concept' NAME
    (':' ancestorListNode)?
    (';' | propertyListNode);

targetNode returns [Target target]:
    'target' NAME propertyListNode;

propertyListNode:
    '{' (propertyNode ';')* '}';

propertyNode returns [Property property]:
    NAME (':' typeNode)? ('=' STRING)?;

ancestorListNode:
    NAME (',' NAME)*;

typeNode returns [Type type]:
    NAME CARDINALITY?;

// Reserved words must precede names. Otherwise, they will be recognized as names.
ABSTRACT:
    'abstract';

NAME:
    ('A'..'Z' | 'a'..'z')
    ( 'A'..'Z' | 'a'..'z' | '0'..'9' | '_' )*;

STRING:
    '"' .*? '"';

CARDINALITY:
    ('?' | '*');

// Ignoring whitespace:
WS:
    ( ' ' | '\t' | '\f' | '\n' | '\r' )+ -> skip;

// Ignoring comments:
COMMENT:
    ('//' .*? '\n' | '(*' .*? '*)' ) -> skip;
