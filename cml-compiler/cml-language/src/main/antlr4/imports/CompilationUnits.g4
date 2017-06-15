grammar CompilationUnits;

import Modules, Concepts, Associations, Tasks, Macros;

compilationUnit:
    declarations*;

declarations
    : moduleDeclaration
    | conceptDeclaration
    | associationDeclaration
    | taskDeclaration
    | macroDeclaration;

