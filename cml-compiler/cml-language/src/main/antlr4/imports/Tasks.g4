grammar Tasks;

import Names, Properties;

taskDeclaration returns [Task task]:
  TASK NAME
  constructorDeclaration?
  (';' | propertyList);

constructorDeclaration: ':' NAME;

