grammar Modules;

import Names;

moduleDeclaration returns [TempModule module]:
    'module' NAME '{' importDeclaration* '}';

importDeclaration returns [Import _import]:
    'import' NAME ';';