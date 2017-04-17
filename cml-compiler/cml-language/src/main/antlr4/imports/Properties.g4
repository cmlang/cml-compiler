grammar Properties;

import Literals, Names, Types;

propertyList:
    '{' (propertyDeclaration ';')* '}';

propertyDeclaration returns [Property property]:
    NAME (':' typeDeclaration)? ('=' STRING)?;

