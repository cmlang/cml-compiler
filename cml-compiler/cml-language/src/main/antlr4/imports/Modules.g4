grammar Modules;

import Names;

moduleDeclaration returns [TempModule module]:
  MODULE NAME '{' importDeclaration* '}';

importDeclaration returns [Import _import]:
  IMPORT NAME ';';