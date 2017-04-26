grammar Concepts;

import Names, Properties;

conceptDeclaration returns [Concept concept]:
    ABSTRACT? 'concept' NAME
    (':' ancestorList)?
    (';' | propertyList);

ancestorList:
    NAME (',' NAME)*;


