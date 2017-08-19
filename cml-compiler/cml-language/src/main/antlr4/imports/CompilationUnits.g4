grammar CompilationUnits;

import Modules, Concepts, Associations, Tasks, Macros, Templates;

compilationUnit:
    declarations*;

declarations
    : moduleDeclaration
    | conceptDeclaration
    | associationDeclaration
    | taskDeclaration
    | macroDeclaration
    | templateDeclaration;

