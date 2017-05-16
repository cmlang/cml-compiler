grammar Concepts;

import Names, Properties;

conceptDeclaration returns [Concept concept]:
    ABSTRACT? 'concept' NAME
    (':' generalizations)?
    (';' | propertyList);

generalizations:
    NAME (',' NAME)*;


