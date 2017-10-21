grammar Properties;

import Names, Types, Expressions;

propertyList:
  '{' (propertyDeclaration ';')* '}';

propertyDeclaration returns [Property property]:
 DERIVED? NAME (':' typeDeclaration)? ('=' expression)?;

DERIVED: '/';