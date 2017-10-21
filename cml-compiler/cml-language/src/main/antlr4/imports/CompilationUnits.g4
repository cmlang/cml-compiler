grammar CompilationUnits;

import Modules, Concepts, Associations, Tasks, Templates;

compilationUnit:
  declarations*;

declarations
  : moduleDeclaration
  | conceptDeclaration
  | associationDeclaration
  | taskDeclaration
  | templateDeclaration;

