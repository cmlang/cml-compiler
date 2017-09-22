grammar Concepts;

import Names, Properties;

conceptDeclaration returns [Concept concept]:
    abstract_='abstract'? 'concept' NAME
    (':' generalizations)?
    (';' | propertyList);

generalizations:
    NAME (',' NAME)*;


