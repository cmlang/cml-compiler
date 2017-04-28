grammar Tasks;

import Names, Properties;

taskDeclaration returns [Task task]:
    'task' NAME
    constructorDeclaration?
    (';' | propertyList);

constructorDeclaration: 'by' NAME;

