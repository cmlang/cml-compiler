grammar Properties;

import Names, Types, Expressions;

propertyList:
    '{' (propertyDeclaration ';')* '}';

propertyDeclaration returns [TempProperty property]:
   DERIVED? NAME (':' typeDeclaration)? ('=' expression)?;

DERIVED: '/';