grammar Concepts;

import Names, Properties;

conceptDeclaration returns [Concept concept]:
    (ABSTRACTION | CONCEPT) NAME
    (':' generalizations)?
    (';' | propertyList);

generalizations:
    NAME (',' NAME)*;


