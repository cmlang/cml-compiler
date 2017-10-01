grammar Concepts;

import Names, Properties;

conceptDeclaration returns [TempConcept concept]:
    (ABSTRACTION | CONCEPT) NAME
    (':' generalizations)?
    (';' | propertyList);

generalizations:
    NAME (',' NAME)*;


