grammar Modules;

import Names;

moduleDeclaration returns [Module module]:
    'module' NAME '{' importDeclaration* '}';

importDeclaration returns [Import _import]:
    'import' NAME ';';