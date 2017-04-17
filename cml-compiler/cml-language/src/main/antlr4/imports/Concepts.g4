grammar Concepts;

import Names, Properties;

conceptDeclaration returns [Concept concept]:
    ABSTRACT? 'concept' NAME
    (':' ancestorListNode)?
    (';' | propertyListNode);

ancestorListNode:
    NAME (',' NAME)*;

ABSTRACT:
    'abstract';
