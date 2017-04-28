grammar Tasks;

import Names, Properties;

taskDeclaration returns [Task task]:
    'task' NAME propertyList;


