grammar Targets;

import Names, Properties;

targetDeclaration returns [Target target]:
    'target' NAME propertyList;


