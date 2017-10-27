grammar CompilationUnits;

import Modules, Concepts, Associations, Tasks, Templates, Functions;

compilationUnit:
  declarations*;

declarations
  : moduleDeclaration
  | conceptDeclaration
  | associationDeclaration
  | taskDeclaration
  | templateDeclaration
  | functionDeclaration;

